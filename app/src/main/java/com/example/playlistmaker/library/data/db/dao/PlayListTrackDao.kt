package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.PlaylistsTrackEntity
import com.example.playlistmaker.library.data.db.TrackToPlaylistEntity
import retrofit2.http.DELETE

@Dao
interface PlayListTrackDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun connectTrackToPlaylist(trackToPlaylistEntity: TrackToPlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(playlistsTrackEntity: PlaylistsTrackEntity)

    @Query("SELECT trackId FROM playlist_tracks_table WHERE playlistId = :playListId")
    suspend fun getTracksIdByPlaylistId(playListId: Int): List<Long>

    @Query("SELECT * FROM playlists_track WHERE trackId = :trackId LIMIT 1")
    suspend fun getTracksById(trackId:Long):PlaylistsTrackEntity

    @Query("SELECT COUNT(*) FROM playlist_tracks_table WHERE trackId = :trackId")
    suspend fun countPlaylistsContainedTrack(trackId: Long):Int

    @Delete
    suspend fun deleteTrackToPlaylist(trackToPlaylistEntity: TrackToPlaylistEntity)

    @Delete
    suspend fun deleteTrack(playlistsTrackEntity: PlaylistsTrackEntity)

}