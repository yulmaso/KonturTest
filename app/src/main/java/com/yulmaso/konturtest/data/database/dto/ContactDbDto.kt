package com.yulmaso.konturtest.data.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yulmaso.konturtest.domain.entity.Contact
import java.util.Calendar

@Entity(tableName = "contacts")
data class ContactDbDto (
    @PrimaryKey
    val id : String,
    val name : String,
    val phone : String,
    val height : Float,
    val biography : String,
    val temperament : String,
    val educationPeriodStart: Calendar,
    val educationPeriodEnd: Calendar
) {
    fun toContact() = Contact(id, name, phone, height, biography, temperament, educationPeriodStart, educationPeriodEnd)
}