package com.yulmaso.konturtest.data.repository

import com.yulmaso.konturtest.data.database.dao.ContactDao
import com.yulmaso.konturtest.data.network.Api
import com.yulmaso.konturtest.data.preferences.DbUtilsPreferences
import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class ContactRepositoryImpl(
    private val api: Api,
    private val contactDao: ContactDao,
    private val dbUtilsPrefs: DbUtilsPreferences
): ContactRepository {

    companion object {
        const val SOURCE_1 = "generated-01.json"
        const val SOURCE_2 = "generated-02.json"
        const val SOURCE_3 = "generated-03.json"

        const val EXPIRATION_PREF = "CONTACTS_CACHE_EXPIRATION_PREF"

        const val CONTACT_CACHE_VALIDITY_DURATION = 1 // Минута
        const val SEARCH_THRESHOLD = 500L // Миллисекунд
    }

    override fun getContacts(forceRefresh: Boolean): Single<List<Contact>> {
        return dbUtilsPrefs.getCacheExpirationTime(EXPIRATION_PREF)
            .map { it ?: Calendar.getInstance().apply { set(Calendar.YEAR, 2000) } }
            .toSingle()
            .flatMap { expirationTime ->
                if (expirationTime > Calendar.getInstance() && !forceRefresh)
                    contactDao.getAll()
                else
                    contactDao.deleteAll()
                        .toSingle { loadAndCacheContacts() }
                        .flatMap { contactDao.getAll() }
            }
            .map { list -> list.map { it.toContact() } }
    }

    override fun searchContacts(query: String): Observable<List<Contact>> {
        return contactDao.searchContacts(query)
            .toObservable()
            .debounce(SEARCH_THRESHOLD, TimeUnit.MILLISECONDS)
            .map { list -> list.map { it.toContact() } }
    }

    private fun loadAndCacheContacts(): Single<Boolean> {
        return Single.merge(
            api.getContacts(SOURCE_1),
            api.getContacts(SOURCE_2),
            api.getContacts(SOURCE_3)
        )
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { list -> contactDao.insert(list.map { it.toDbDto() }) }
            .andThen {
                dbUtilsPrefs.setCacheInsertTime(
                    EXPIRATION_PREF,
                    Calendar.getInstance(),
                    CONTACT_CACHE_VALIDITY_DURATION
                )
            }
            .toSingle { true }
    }
}