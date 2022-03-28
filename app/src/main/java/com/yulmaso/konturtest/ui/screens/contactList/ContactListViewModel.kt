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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactListViewModel(
    app: Application,
    private val router: Router,
    private val contactUseCase: ContactUseCase
): BaseViewModel(app) {

    private val contactsMLive = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = contactsMLive

    val searchInput = MutableLiveData<String>()

    private val errorMLive = MutableLiveData<String>()
    val error: LiveData<String> = errorMLive

    // По дефотлу - true, потому что отображается только при первой загрузке
    private val progressBarVisibilityMLive = MutableLiveData(true)
    val progressBarVisibility: LiveData<Boolean> = progressBarVisibilityMLive

    val isRefreshing = ObservableBoolean()

    init {
        refreshData()
    }

    fun refreshData() {
        contactUseCase.getContacts(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isRefreshing.set(true)
                addDisposable(it)
            }
            .doAfterTerminate {
                isRefreshing.set(false)
                progressBarVisibilityMLive.value = false
            }
            .subscribe(
                { contactsMLive.value = it },
                { errorMLive.postValue(getApplication<App>().getString(R.string.network_error)) }
            )
    }
    
    fun onContactClick(contact: Contact) {
        router.navigateTo(Screens.contactDetails(contact))
    }

    fun onClearClick() {
        searchInput.value = ""
    }

}