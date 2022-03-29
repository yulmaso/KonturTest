package com.yulmaso.konturtest.data.preferences

import android.content.SharedPreferences
import com.yulmaso.konturtest.STORAGE_DATE_FORMAT_PATTERN
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.*

class DbUtilsPreferencesImpl(
    private val sharedPrefs: SharedPreferences
) : DbUtilsPreferences {

    override fun getCacheInsertTime(tag: String): Single<Calendar> {
        return Single.create { emitter ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sharedPrefs.getLong(tag + "_value", 0)
            calendar.timeZone = TimeZone.getTimeZone(
                sharedPrefs.getString(tag + "_zone", TimeZone.getDefault().id)
            )
            emitter.onSuccess(calendar)
        }
    }

    override fun setCacheInsertTime(tag: String, value: Calendar): Completable {
        return Completable.fromAction {
            sharedPrefs.edit()
                .putLong(tag + "_value", value.timeInMillis)
                .putString(tag + "_zone", value.timeZone.id)
                .apply()
        }
    }
}