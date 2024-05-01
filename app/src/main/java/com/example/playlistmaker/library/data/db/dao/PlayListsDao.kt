package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.PlayListsEntity

@Dao
interface PlayListsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playListsEntity: PlayListsEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlayListsList(): List<PlayListsEntity>

}