package com.yulmaso.konturtest.data.repository

import android.util.Log
import com.yulmaso.konturtest.data.database.dao.ContactDao
import com.yulmaso.konturtest.data.network.Api
import com.yulmaso.konturtest.data.preferences.DbUtilsPreferences
import com.yulmaso.konturtest.domain.entity.Contact
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

    override fun getContacts(forceRefresh: Boolean, limit: Int, offset: Int): Single<List<Contact>> {
        return dbUtilsPrefs.getCacheInsertTime(CONTACTS_EXPIRATION_TAG)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable {
                val expirationTime = it.apply { add(Calendar.MINUTE, CACHE_TIMEOUT) }
                Log.d("REP_TAG", "Need to reload = " +
                        "${offset == 0 && expirationTime < Calendar.getInstance() || forceRefresh}")
                if (offset == 0 && expirationTime < Calendar.getInstance() || forceRefresh)
                    loadAndCacheContacts()
                else
                    Completable.fromAction {}
            }
            .andThen(contactDao.getPagedContacts(limit, offset))
            .doOnSuccess { Log.d("REP_TAG", "GOT PAGE FROM DB") }
            .map { list -> list.map { it.toContact() } }
    }

    override fun searchContacts(query: String): Single<List<Contact>> {
        return contactDao.searchContacts(query)
            .map { list -> list.map { it.toContact() } }
    }

    private fun loadAndCacheContacts(): Completable {
        return contactDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .doOnComplete { Log.d("REP_TAG", "CLEANING DB") }
            .andThen(
                Single.merge(
                    api.getContacts(SOURCE_1),
                    api.getContacts(SOURCE_2),
                    api.getContacts(SOURCE_3)
                )
            )
            .doOnNext { Log.d("REP_TAG", "DOWNLOADED CHUNK") }
            .flatMapCompletable { list ->
                Log.d("REP_TAG", "PUT IN DB")
                contactDao.insert(list.map { it.toDbDto() })
            }
            .doFinally {
                dbUtilsPrefs.setCacheInsertTime(
                    CONTACTS_EXPIRATION_TAG,
                    Calendar.getInstance()
                )
            }
    }
}