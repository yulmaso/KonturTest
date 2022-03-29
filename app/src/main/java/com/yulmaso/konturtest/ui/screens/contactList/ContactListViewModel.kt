package com.yulmaso.konturtest.ui.screens.contactList

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import com.yulmaso.konturtest.App
import com.yulmaso.konturtest.R
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.domain.useCase.ContactUseCase
import com.yulmaso.konturtest.ui.BaseViewModel
import com.yulmaso.konturtest.ui.navigation.Screens
import com.yulmaso.konturtest.utils.Pagination
import com.yulmaso.konturtest.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ContactListViewModel(
    app: Application,
    private val router: Router,
    private val contactUseCase: ContactUseCase
): BaseViewModel(app) {

    private val progressBarVisibilityMLive = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = progressBarVisibilityMLive

    val isRefreshing = ObservableBoolean()

    private val contactsFullMutable = ArrayList<Contact>()
    val contactsFull: List<Contact> = contactsFullMutable

    val contactsToAdd = SingleLiveEvent<List<Contact>?>()

    val searchInput = SingleLiveEvent<String>()

    val error = SingleLiveEvent<String?>()

    private val paginator = PublishProcessor.create<Pagination>()
        .also { subscribeForContacts(it) }

    val paginationState = Pagination()

    init {
        loadData(Pagination.LoadType.FIRST_LOAD)
    }

    fun loadData(loadType: Pagination.LoadType, searchInput: String? = null) {
        paginator.onNext(paginationState.startLoad(loadType, searchInput))
    }

    private fun subscribeForContacts(paginator: PublishProcessor<Pagination>) {
        paginator
            .filter { !it.noMoreData }
            .doOnNext { if (it.loadType != Pagination.LoadType.SCROLL_LOAD) showProgress() }
            .debounce(80, TimeUnit.MILLISECONDS)
            .flatMapSingle { paginationState ->
                contactUseCase.getContacts(paginationState)
                    .subscribeOn(Schedulers.io())
                    .doOnSuccess { paginationState.stopLoad(itemCount = it.size) }
                    .doOnError {
                        paginationState.stopLoad(failureOccured = true)

                        // Не было времени реализовывать нормальную обработку ошибок
                        error.postValue(getApplication<App>().getString(R.string.network_error))

                        // Костыль чтобы при неудачном обновлении кеша на старте МП все-таки
                        // отображались старые данные кеша
                        if (paginationState.loadType == Pagination.LoadType.FIRST_LOAD)
                            loadData(Pagination.LoadType.SCROLL_LOAD)
                    }
                    .doAfterTerminate { hideProgress() }
                    .onErrorReturn { listOf() }
                    .map { items -> paginationState to items }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (paginationState, items) ->
                if (paginationState.failureOccured) return@subscribe
                if (paginationState.loadType != Pagination.LoadType.SCROLL_LOAD) {
                    contactsFullMutable.clear()
                    contactsFullMutable.addAll(items)
                    contactsToAdd.call()
                } else {
                    contactsFullMutable.addAll(items)
                    contactsToAdd.postValue(items)
                }
            }
            .also { addDisposable(it) }
    }

    fun onRefreshSwipe() {
        loadData(Pagination.LoadType.REFRESH_LOAD, searchInput.value)
    }
    
    fun onContactClick(contact: Contact) {
        router.navigateTo(Screens.contactDetails(contact))
    }

    fun onClearClick() {
        searchInput.value = ""
    }

    private fun showProgress() {
        isRefreshing.set(true)
        isRefreshing.set(false)
        progressBarVisibilityMLive.postValue(true)
    }

    private fun hideProgress() {
        progressBarVisibilityMLive.postValue(false)
    }

}