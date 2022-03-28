package com.yulmaso.konturtest.data.network

import com.yulmaso.konturtest.data.network.dto.ContactNwDto
import com.yulmaso.konturtest.domain.entity.Contact
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("master/json/{source}")
    fun getContacts(
        @Path("source") sourceName: String
    ): Single<List<ContactNwDto>>


}