package com.yulmaso.konturtest.data.repository

import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.utils.Pagination
import io.reactivex.rxjava3.core.Single

interface ContactRepository {
    fun getContacts(pagination: Pagination): Single<List<Contact>>
}