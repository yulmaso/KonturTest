package com.yulmaso.konturtest.domain.useCase

import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContactUseCase {
    fun getContacts(forceRefresh: Boolean): Single<List<Contact>>
    fun searchContacts(query: String): Observable<List<Contact>>
}