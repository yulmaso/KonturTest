package com.yulmaso.konturtest.data.database.dao

import androidx.room.*
import com.yulmaso.konturtest.data.database.dto.ContactDbDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ContactDbDto>): Completable

    @Query("DELETE FROM contacts")
    fun deleteAll(): Completable

    @Query("SELECT * FROM contacts " +
            "WHERE name LIKE '%'||:query||'%' OR phone LIKE '%'||:query||'%' " +
            "ORDER BY id LIMIT :limit OFFSET :offset")
    fun getPagedContacts(limit: Int, offset: Int, query: String): Single<List<ContactDbDto>>
}