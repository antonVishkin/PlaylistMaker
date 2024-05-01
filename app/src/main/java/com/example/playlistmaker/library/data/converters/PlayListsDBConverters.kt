package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.PlayListsEntity
import com.example.playlistmaker.library.domain.Playlist

class PlayListsDBConverters {
    fun map(playlist: Playlist): PlayListsEntity =
        PlayListsEntity(
            0,
            playlist.name,
            playlist.description,
            playlist.imagePath
        )

    fun map(playListsEntity: PlayListsEntity, trackList: List<Int>): Playlist = Playlist(
        playListsEntity.name,
        playListsEntity.description,
        playListsEntity.imageFilePath,
        trackList
    )
}