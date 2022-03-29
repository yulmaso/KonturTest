package com.yulmaso.konturtest.domain.useCase

import com.yulmaso.konturtest.data.repository.ContactRepository
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.utils.Pagination
import io.reactivex.rxjava3.core.Single

class ContactUseCaseImpl(
    private val contactRepository: ContactRepository
): ContactUseCase {

    override fun getContacts(pagination: Pagination): Single<List<Contact>> {
        return contactRepository.getContacts(pagination)
    }

}