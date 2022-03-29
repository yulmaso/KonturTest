package com.yulmaso.konturtest.ui

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.yulmaso.konturtest.ui.navigation.Screens

class MainActivityViewModel(
    private val router: Router
): ViewModel() {

    private var initialized = false

    fun onCreate() {
        if (!initialized) {
            initialized = true
            router.newRootScreen(Screens.contactList())
        }
    }
}