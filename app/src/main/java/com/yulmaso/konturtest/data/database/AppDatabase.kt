package com.yulmaso.konturtest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yulmaso.konturtest.data.database.dao.ContactDao
import com.yulmaso.konturtest.data.database.dto.ContactDbDto

@Database(entities = [
    ContactDbDto::class
], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}