package com.yulmaso.konturtest

import android.app.Application
import android.util.Log
import com.yulmaso.konturtest.di.DI
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(DI.allModules())
        }

        RxJavaPlugins.setErrorHandler {
            if (it is UndeliverableException) {
                // Merely log undeliverable exceptions
                Log.d(null, null, it)
            } else {
                // Forward all others to current thread's uncaught exception handler
                Thread.currentThread().also { thread ->
                    thread.uncaughtExceptionHandler.uncaughtException(thread, it)
                }
            }
        }
    }
}