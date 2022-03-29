package com.yulmaso.konturtest.data.repository

import android.text.format.DateFormat
import android.util.Log
import com.yulmaso.konturtest.LOG_TAG
import com.yulmaso.konturtest.STORAGE_DATE_FORMAT_PATTERN
import com.yulmaso.konturtest.data.database.dao.ContactDao
import com.yulmaso.konturtest.data.network.Api
import com.yulmaso.konturtest.data.preferences.DbUtilsPreferences
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.utils.Pagination
import com.yulmaso.konturtest.utils.doOnFirst
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class ContactRepositoryImpl(
    private val api: Api,
    private val contactDao: ContactDao,
    private val dbUtilsPrefs: DbUtilsPreferences
): ContactRepository {

    companion object {
        const val SOURCE_1 = "generated-01.json"
        const val SOURCE_2 = "generated-02.json"
        const val SOURCE_3 = "generated-03.json"

        const val CONTACTS_EXPIRATION_TAG = "CONTACTS_EXPIRATION_TAG"

        const val CACHE_TIMEOUT = 1 // Минута
    }

    override fun getContacts(pagination: Pagination): Single<List<Contact>> {
        return dbUtilsPrefs.getCacheInsertTime(CONTACTS_EXPIRATION_TAG)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable {
                val expirationTime = it.apply { add(Calendar.MINUTE, CACHE_TIMEOUT) }

                // Обновляем кеш только на старте приложения И только когда он протух ИЛИ по свайпу
                val needToUpdateCache = pagination.loadType == Pagination.LoadType.FIRST_LOAD &&
                            expirationTime < Calendar.getInstance() ||
                        pagination.loadType == Pagination.LoadType.REFRESH_LOAD

//                Log.d(LOG_TAG, "Need To Update Cache = $needToUpdateCache, " +
//                        "limit = ${pagination.limit}, offset = ${pagination.offset}")

                if (needToUpdateCache) fetchAndCacheContacts() else Completable.fromAction {}
            }
            .andThen(
                with(pagination) { contactDao.getPagedContacts(limit, offset, searchInput) }
            )
//            .doOnSuccess { Log.d(LOG_TAG, "GOT PAGE FROM DB") }
            .map { list -> list.map { it.toContact() } }
    }

    private fun fetchAndCacheContacts(): Completable {
        return Single.merge(
            api.getContacts(SOURCE_1),
            api.getContacts(SOURCE_2),
            api.getContacts(SOURCE_3)
        )
//            .doOnNext { Log.d(LOG_TAG, "DOWNLOAD SUCCESS") }
            .doOnFirst { contactDao.deleteAll() }
            .flatMapCompletable { list ->
//                Log.d(LOG_TAG, "PUT IN DB")
                contactDao.insert(list.map { it.toDbDto() })
            }
            .andThen(
                dbUtilsPrefs.setCacheInsertTime(
                    CONTACTS_EXPIRATION_TAG,
                    Calendar.getInstance()
                )
            )
//            .doOnComplete { Log.d(LOG_TAG, "SAVED_CACHE_INSERT_TIME = " +
//                    "${DateFormat.format(STORAGE_DATE_FORMAT_PATTERN, Calendar.getInstance())}") }
    }
}