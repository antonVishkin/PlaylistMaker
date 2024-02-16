package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.player.data.track.Track

data class SearchResponse(val resultCount: Int, val results: ArrayList<Track>) : Response()