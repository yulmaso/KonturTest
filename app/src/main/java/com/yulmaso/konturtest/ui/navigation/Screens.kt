package com.yulmaso.konturtest.ui.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.ui.screens.contactDetails.ContactDetailsFragment
import com.yulmaso.konturtest.ui.screens.contactList.ContactListFragment

object Screens {
    fun contactList() =
        FragmentScreen { ContactListFragment.newInstance() }
    fun contactDetails(contact: Contact) =
        FragmentScreen { ContactDetailsFragment.newInstance(contact) }
}