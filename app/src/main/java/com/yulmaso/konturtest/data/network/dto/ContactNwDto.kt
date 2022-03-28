package com.yulmaso.konturtest.data.network.dto

import com.yulmaso.konturtest.data.database.dto.ContactDbDto
import com.yulmaso.konturtest.domain.entity.Contact
import java.util.Calendar

data class ContactNwDto(
    val id : String,
    val name : String,
    val phone : String,
    val height : Float,
    val biography : String,
    val temperament : String,
    val educationPeriod: EducationPeriod
) {
    class EducationPeriod(
        val start: Calendar,
        val end: Calendar
    )

    fun toContact() = Contact(id, name, phone, height, biography, temperament, educationPeriod.start, educationPeriod.end)
    fun toDbDto() = ContactDbDto(id, name, phone, height, biography, temperament, educationPeriod.start, educationPeriod.end)
}