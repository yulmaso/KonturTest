package com.yulmaso.konturtest.di

import com.yulmaso.konturtest.di.data.NetworkModule
import com.yulmaso.konturtest.di.data.RepositoryModule
import com.yulmaso.konturtest.di.data.StorageModule
import com.yulmaso.konturtest.di.domain.UseCaseModule
import com.yulmaso.konturtest.di.ui.AppModule
import com.yulmaso.konturtest.di.ui.ViewModelModule
import org.koin.core.module.Module

object DI {

    fun allModules(): List<Module> {
        return listOf(
            AppModule.create(),
            StorageModule.create(),
            NetworkModule.create(),
            ViewModelModule.create(),
            UseCaseModule.create(),
            RepositoryModule.create()
        )
    }

}