package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.PlaylistTrackEntity

@Dao
interface PlayListTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT trackId FROM playlist_tracks_table WHERE playlistId = :playListId")
    suspend fun getTracksByPlaylistId(playListId: Int): List<Int>
}