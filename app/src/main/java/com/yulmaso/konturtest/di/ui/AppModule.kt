package com.yulmaso.konturtest.di.ui

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.dsl.module

object AppModule {

    fun create() = module {
        single(createdAtStart = true) { provideCicerone() }
        single { provideRouter(get()) }
        single { provideNavigatorHolder(get()) }
    }

    private fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create(Router())
    }

    private fun provideRouter(cicerone: Cicerone<Router>): Router {
        return cicerone.router
    }

    private fun provideNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

}