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
import com.yulmaso.konturtest.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class ContactListViewModel(
    app: Application,
    private val router: Router,
    private val contactUseCase: ContactUseCase
): BaseViewModel(app) {

    companion object {
        private const val PAGE_ITEM_COUNT = 50
    }

    // По дефотлу - true, потому что отображается только при первой загрузке
    private val progressBarVisibilityMLive = MutableLiveData(true)
    val progressBarVisibility: LiveData<Boolean> = progressBarVisibilityMLive

    val isRefreshing = ObservableBoolean()

    private val contactsFullMutable = LinkedList<Contact>()
    val contactsFull: List<Contact> = contactsFullMutable

    val contactsToAdd = SingleLiveEvent<List<Contact>?>()

    val searchInput = MutableLiveData<String>()
    val error = SingleLiveEvent<String?>()

    // Pair<Int, Boolean> - Int: limit, Boolean: forceRefresh
    private val paginator = PublishProcessor.create<Pair<Int, Boolean>>()
        .also { subscribeForContacts(it) }

    init {
        loadMore()
    }

    var noMoreData = false
        private set
    private var offset = 0

    fun refreshData() {
        noMoreData = false
        offset = 0

        paginator.onNext(PAGE_ITEM_COUNT to true)
    }

    fun loadMore() {
        paginator.onNext(PAGE_ITEM_COUNT to false)
    }

    private fun subscribeForContacts(paginator: PublishProcessor<Pair<Int, Boolean>>) {
        paginator
            .filter { !noMoreData }
            .doOnNext {
                showProgress()
            }
            .debounce(100, TimeUnit.MILLISECONDS)
            .flatMapSingle { (limit, forceRefresh) ->
                contactUseCase.getContacts(forceRefresh, limit, offset)
                    .subscribeOn(Schedulers.io())
                    .doOnError { error.postValue(getApplication<App>().getString(R.string.network_error)) }
                    .onErrorReturn { listOf() }
                    .doAfterTerminate { hideProgress() }
                    .map { limit to it }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (limit, items) ->
                if (offset == 0) {
                    contactsFullMutable.clear()
                    contactsFullMutable.addAll(items)
                    contactsToAdd.value = null
                } else {
                    contactsFullMutable.addAll(items)
                    contactsToAdd.value = items
                }
                noMoreData =  items.size < limit - 1
                offset += items.size
            }
    }
    
    fun onContactClick(contact: Contact) {
        router.navigateTo(Screens.contactDetails(contact))
    }

    fun onClearClick() {
        searchInput.value = ""
    }

    private fun showProgress() {
        isRefreshing.set(true)
    }

    private fun hideProgress() {
        isRefreshing.set(false)
        progressBarVisibilityMLive.postValue(false)
    }

}