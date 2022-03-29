package com.yulmaso.konturtest.data.preferences

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.*

interface DbUtilsPreferences {
    fun getCacheInsertTime(tag: String): Single<Calendar>
    fun setCacheInsertTime(tag: String, value: Calendar): Completable
}