package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.PlayListsEntity
import com.example.playlistmaker.library.data.db.PlaylistsTrackEntity
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.player.domain.Track

class PlayListsDBConverters {

    fun map(playListsEntity: PlayListsEntity, trackList: List<Track>): Playlist = Playlist(
        id = playListsEntity.id,
        name = playListsEntity.name,
        description = playListsEntity.description,
        imagePath = playListsEntity.imageFilePath,
        list = trackList
    )

    fun map(track: Track):PlaylistsTrackEntity = PlaylistsTrackEntity(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTimeMillis = track.trackTimeMillis,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        artworkUrl100 = track.artworkUrl100,
        country = track.country,
        previewUrl = track.previewUrl
    )

    fun map(trackEntity: PlaylistsTrackEntity):Track = Track(
        trackId = trackEntity.trackId,
        trackName = trackEntity.trackName,
        artistName = trackEntity.artistName,
        trackTimeMillis = trackEntity.trackTimeMillis,
        collectionName = trackEntity.collectionName,
        releaseDate = trackEntity.releaseDate,
        primaryGenreName = trackEntity.primaryGenreName,
        artworkUrl100 = trackEntity.artworkUrl100,
        country = trackEntity.country,
        previewUrl = trackEntity.previewUrl
    )
}