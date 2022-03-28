package com.yulmaso.konturtest.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yulmaso.konturtest.STORAGE_DATE_FORMAT_PATTERN
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class CalendarDeserializer: JsonDeserializer<Calendar> {

    private val calendarFormat by lazy {
        SimpleDateFormat(STORAGE_DATE_FORMAT_PATTERN, Locale.getDefault())
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Calendar {
        val s = json.asString
        s.let {
            val calendar = Calendar.getInstance()
            calendar.time = calendarFormat.parse(it)!!
            return calendar
        }
    }
}