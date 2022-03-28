package com.yulmaso.konturtest.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Contact(
    val id : String,
    val name : String,
    val phone : String,
    val height : Float,
    val biography : String,
    val temperament : String,
    val educationPeriodStart : Calendar,
    val educationPeriodEnd: Calendar
): Parcelable {
    val heightStr
        get() = height.toString()
}