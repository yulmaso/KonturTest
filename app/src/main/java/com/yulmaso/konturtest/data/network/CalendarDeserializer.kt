package com.yulmaso.konturtest.data.network

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yulmaso.konturtest.STORAGE_DATE_FORMAT_PATTERN
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class CalendarDeserializer: JsonDeserializer<Calendar> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Calendar {
        val s = json.asString
        val calendarFormat = SimpleDateFormat(STORAGE_DATE_FORMAT_PATTERN, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            calendar.time = calendarFormat.parse(s)!!
        } catch (e: Exception) {
            Log.e("LOG_TAG", "JSON_ERROR", e)
            calendar.timeInMillis = 0
        }
        return calendar
    }
}