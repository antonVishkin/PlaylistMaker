package com.example.playlistmaker.library.data

import android.util.Log
import com.example.playlistmaker.library.data.converters.PlayListsDBConverters
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.PlayListsEntity
import com.example.playlistmaker.library.data.db.TrackToPlaylistEntity
import com.example.playlistmaker.library.domain.playlist.PlayListsRepository
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListsDBConverters: PlayListsDBConverters
) : PlayListsRepository {
    override suspend fun addPlayList(name: String, description: String, imagePath: String) {
        appDatabase.playListsDao().addPlayList(
            PlayListsEntity(
                id = 0,
                name = name,
                description = description,
                imageFilePath = imagePath
            )
        )
    }

    override suspend fun getPlaylistsList(): Flow<List<Playlist>> = flow {
        val playListsList = appDatabase.playListsDao().getPlayListsList().map {
            var trackList: List<Track>
            try {
                trackList = getTrackListByPlaylistId(it.id)
            } catch (e: Exception) {
                trackList = listOf()
            }
            playListsDBConverters.map(it, trackList)
        }
        emit(playListsList)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        if (appDatabase.playListTrackDao()
                .istPlaylistsContainedTrack(trackId = track.trackId, playlistId = playlist.id) != 0
        )
            return false
        try {
            appDatabase.playListTrackDao()
                .connectTrackToPlaylist(TrackToPlaylistEntity(0, playlist.id, track.trackId))
            appDatabase.playListTrackDao()
                .addTrackToPlaylist(playListsDBConverters.map(track))
            return true
        } catch (e: Throwable) {
            return false
        }
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist): Playlist {
        appDatabase.playListTrackDao()
            .deleteTrackToPlaylist(
                trackId = track.trackId,
                playlistId = playlist.id
            )
        val noTrackInPlaylists = appDatabase.playListTrackDao()
            .countPlaylistsContainedTrack(trackId = track.trackId) == 0
        if (noTrackInPlaylists)
            appDatabase.playListTrackDao().deleteTrack(playListsDBConverters.map(track))
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            getTrackListByPlaylistId(playlistId = playlist.id)
        )
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        playlist.list.forEach {
            removeTrackFromPlaylist(it, playlist)
        }
        appDatabase.playListsDao().deletePlaylist(playListsDBConverters.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playListsDao().updatePlaylist(playListsDBConverters.map(playlist = playlist))
    }

    override suspend fun getPlayListById(playlistId: Int): Playlist {
        val playlistEntity = appDatabase.playListsDao().getPlayListsById(playlistId)
        return playListsDBConverters.map(playlistEntity, getTrackListByPlaylistId(playlistId))
    }


    private suspend fun getTrackListByPlaylistId(playlistId: Int): List<Track> {
        return appDatabase.playListTrackDao().getTracksIdByPlaylistId(playlistId)
            .sortedByDescending { it.id }.map {
            Log.v("PLAYLIST", "${it.trackId}")
            playListsDBConverters.map(appDatabase.playListTrackDao().getTracksById(it.trackId))
        }
    }
}