package com.yulmaso.konturtest.domain.useCase

import com.yulmaso.konturtest.data.repository.ContactRepository
import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Single

class ContactUseCaseImpl(
    private val contactRepository: ContactRepository
): ContactUseCase {

    override fun getContacts(forceRefresh: Boolean, limit: Int, offset: Int): Single<List<Contact>> {
        return contactRepository.getContacts(forceRefresh, limit, offset)
    }

    override fun searchContacts(query: String): Single<List<Contact>> {
        return contactRepository.searchContacts(query)
    }
}