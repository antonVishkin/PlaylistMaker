package com.example.playlistmaker.search.data.dto


data class SearchResponse(val resultCount: Int, val results: ArrayList<TrackDto>) : Response()