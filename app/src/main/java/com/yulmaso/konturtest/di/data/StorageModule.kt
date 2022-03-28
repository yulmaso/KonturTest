package com.yulmaso.konturtest.di.data

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.yulmaso.konturtest.data.database.AppDatabase
import com.yulmaso.konturtest.data.database.dao.ContactDao
import com.yulmaso.konturtest.data.preferences.DbUtilsPreferences
import com.yulmaso.konturtest.data.preferences.DbUtilsPreferencesImpl
import org.koin.dsl.module

object StorageModule {

    private const val DB_UTILS_PREFERENCES          = "DB_UTILS_PREFERENCES"

    fun create() = module {
        single(createdAtStart = true) { provideDatabase(get()) }
        single { provideContactDao(get()) }
        single { provideDbUtilsPrefs(get()) }
    }

    private fun provideDatabase(ctx: Context): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "KonturTestDb").build()
    }

    private fun provideContactDao(db: AppDatabase): ContactDao {
        return db.contactDao()
    }

    private fun provideDbUtilsPrefs(app: Application): DbUtilsPreferences {
        val prefs = app.getSharedPreferences(DB_UTILS_PREFERENCES, Context.MODE_PRIVATE)
        return DbUtilsPreferencesImpl(prefs)
    }

}