package com.yulmaso.konturtest.di.domain

import com.yulmaso.konturtest.domain.useCase.ContactUseCase
import com.yulmaso.konturtest.domain.useCase.ContactUseCaseImpl
import org.koin.dsl.module

object UseCaseModule {

    fun create() = module {
        single<ContactUseCase> { ContactUseCaseImpl(get()) }
    }

}