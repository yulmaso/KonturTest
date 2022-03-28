package com.yulmaso.konturtest.di.data

import com.yulmaso.konturtest.data.repository.ContactRepository
import com.yulmaso.konturtest.data.repository.ContactRepositoryImpl
import org.koin.dsl.module

object RepositoryModule {

    fun create() = module {
        single<ContactRepository> { ContactRepositoryImpl(get(), get(), get()) }
    }

}