package com.yulmaso.konturtest.domain.useCase

import com.yulmaso.konturtest.data.repository.ContactRepository
import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class ContactUseCaseImpl(
    private val contactRepository: ContactRepository
): ContactUseCase {

    override fun getContacts(forceRefresh: Boolean): Single<List<Contact>> {
        return contactRepository.getContacts(forceRefresh)
    }

    override fun searchContacts(query: String): Observable<List<Contact>> {
        return contactRepository.searchContacts(query)
    }
}