package com.yulmaso.konturtest.data.preferences

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import java.util.*

interface DbUtilsPreferences {
    /**
     * Возвращает время, в которое кеш уже невалиден
     */
    fun getCacheExpirationTime(prefName: String): Maybe<Calendar>

    /**
     * Сохраняет время валидности данных в кеше
     *
     * @param time          - время в момент записи данных в бд
     * @param validDuration - длительность валидности кеша (в минутах)
     */
    fun setCacheInsertTime(prefName: String, time: Calendar, validDuration: Int): Completable

    /**
     * Сбрасывает время валидности данных в кеше
     */
    fun resetCacheExpirationTime(prefName: String): Completable
}