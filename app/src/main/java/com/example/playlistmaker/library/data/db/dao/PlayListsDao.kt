package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.library.data.db.PlayListsEntity

@Dao
interface PlayListsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playListsEntity: PlayListsEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlayListsList(): List<PlayListsEntity>

    @Query("SELECT * FROM playlists_table WHERE id = :playlistId LIMIT 1")
    suspend fun getPlayListsById(playlistId :Int): PlayListsEntity


    @Delete
    suspend fun deletePlaylist(playListsEntity: PlayListsEntity)

    @Update
    suspend fun updatePlaylist(playListsEntity: PlayListsEntity)
}