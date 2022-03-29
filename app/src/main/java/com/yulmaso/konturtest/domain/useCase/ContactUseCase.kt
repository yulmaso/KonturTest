package com.yulmaso.konturtest.domain.useCase

import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.utils.Pagination
import io.reactivex.rxjava3.core.Single

interface ContactUseCase {
    fun getContacts(pagination: Pagination): Single<List<Contact>>
}