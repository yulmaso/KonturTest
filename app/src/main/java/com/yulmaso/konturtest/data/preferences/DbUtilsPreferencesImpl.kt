package com.yulmaso.konturtest.data.preferences

import android.content.SharedPreferences
import com.yulmaso.konturtest.STORAGE_DATE_FORMAT_PATTERN
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import java.text.SimpleDateFormat
import java.util.*

class DbUtilsPreferencesImpl(
    private val sharedPrefs: SharedPreferences
) : DbUtilsPreferences {

    private val calendarFormat by lazy {
        SimpleDateFormat(STORAGE_DATE_FORMAT_PATTERN, Locale.getDefault())
    }

    override fun getCacheExpirationTime(prefName: String): Maybe<Calendar> {
        return Maybe.create { emitter ->
            val time = sharedPrefs.getString(prefName, null)
            time?.let {
                val calendar = Calendar.getInstance().apply {
                    setTime(calendarFormat.parse(it)!!)
                }
                emitter.onSuccess(calendar)
            } ?: emitter.onComplete()
        }
    }

    override fun setCacheInsertTime(
        prefName: String,
        time: Calendar,
        validDuration: Int
    ): Completable {
        return Completable.fromAction {
            val expirationTime = time.add(Calendar.SECOND, validDuration)
            sharedPrefs
                .edit()
                .putString(prefName, calendarFormat.format(expirationTime))
                .apply()
        }
    }

    override fun resetCacheExpirationTime(prefName: String): Completable {
        return Completable.fromAction {
            sharedPrefs
                .edit()
                .putString(prefName, null)
                .apply()
        }
    }
}