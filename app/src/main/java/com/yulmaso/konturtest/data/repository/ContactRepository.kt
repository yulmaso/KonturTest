package com.yulmaso.konturtest.data.repository

import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Single

interface ContactRepository {
    fun getContacts(forceRefresh: Boolean, limit: Int, offset: Int): Single<List<Contact>>
    fun searchContacts(query: String): Single<List<Contact>>
}