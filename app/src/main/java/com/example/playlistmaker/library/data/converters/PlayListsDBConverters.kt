package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.PlayListsEntity
import com.example.playlistmaker.library.domain.playlist.Playlist

class PlayListsDBConverters {
    fun map(playlist: Playlist): PlayListsEntity =
        PlayListsEntity(
            id = 0,
            name = playlist.name,
            description = playlist.description,
            imageFilePath = playlist.imagePath
        )

    fun map(playListsEntity: PlayListsEntity, trackList: List<Int>): Playlist = Playlist(
        id = playListsEntity.id,
        name = playListsEntity.name,
        description = playListsEntity.description,
        imagePath = playListsEntity.imageFilePath,
        list = trackList
    )
}