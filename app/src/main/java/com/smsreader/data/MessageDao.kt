package com.smsreader.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages order by created desc")
    fun getAll(): List<Messages>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Messages>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Messages)

    @Query("SELECT * FROM messages WHERE uid = :id")
    fun getMessage(id: Int): Messages
}