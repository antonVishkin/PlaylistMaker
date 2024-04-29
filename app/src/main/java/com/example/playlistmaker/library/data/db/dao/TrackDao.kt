package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavorite(track: TrackEntity)

    @Query("SELECT * FROM track_favorites")
    suspend fun getFavoriteList(): List<TrackEntity>

    @Query("SELECT trackId FROM track_favorites")
    suspend fun getFavoritesIdList(): List<Long>

    @Delete(entity = TrackEntity::class)
    suspend fun removeTrackFromFavorites(track: TrackEntity)

    @Query("SELECT COUNT(*) as count FROM track_favorites WHERE trackId = :trackId")
    suspend fun isFavorite(trackId: Long): Int

    @Query("SELECT * FROM track_favorites WHERE trackId = :trackId")
    suspend fun getFavoriteById(trackId: Long): TrackEntity
}