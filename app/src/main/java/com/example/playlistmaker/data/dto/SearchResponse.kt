package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.entity.Track

data class SearchResponse(val resultCount: Int, val results: ArrayList<Track>) : Response()