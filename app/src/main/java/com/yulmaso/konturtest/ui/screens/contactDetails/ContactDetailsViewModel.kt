package com.yulmaso.konturtest.ui.screens.contactDetails

import android.app.Application
import android.text.format.DateFormat
import com.github.terrakok.cicerone.Router
import com.yulmaso.konturtest.UI_DATE_FORMAT_PATTERN
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.ui.BaseViewModel

class ContactDetailsViewModel(
    app: Application,
    private val router: Router,
    val contact: Contact
): BaseViewModel(app) {

    val educationPeriodStr: String
        get() {
            val start = DateFormat.format(UI_DATE_FORMAT_PATTERN, contact.educationPeriodStart)
            val end = DateFormat.format(UI_DATE_FORMAT_PATTERN, contact.educationPeriodEnd)
            return "$start - $end"
        }

    fun onBackBtnClick() {
        router.exit()
    }

}