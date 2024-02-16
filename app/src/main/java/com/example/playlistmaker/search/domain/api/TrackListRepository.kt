package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.player.data.track.Track

interface TrackListRepository {
    fun searchTrack(expression:String): Resource<List<Track>>
}