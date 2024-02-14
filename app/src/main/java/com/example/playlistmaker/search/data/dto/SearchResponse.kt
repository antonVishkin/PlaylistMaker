package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.domain.entity.Track

data class SearchResponse(val resultCount: Int, val results: ArrayList<Track>) : Response()