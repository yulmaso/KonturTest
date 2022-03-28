package com.yulmaso.konturtest.di.ui

import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.ui.screens.contactDetails.ContactDetailsViewModel
import com.yulmaso.konturtest.ui.screens.contactList.ContactListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

    fun create() = module {
        viewModel { (contact: Contact) -> ContactDetailsViewModel(get(), get(), contact) }
        viewModel { ContactListViewModel(get(), get(), get()) }
    }

}