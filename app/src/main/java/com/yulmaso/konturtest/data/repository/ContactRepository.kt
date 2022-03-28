package com.yulmaso.konturtest.data.repository

import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContactRepository {
    fun getContacts(forceRefresh: Boolean): Single<List<Contact>>
    fun searchContacts(query: String): Observable<List<Contact>>
}