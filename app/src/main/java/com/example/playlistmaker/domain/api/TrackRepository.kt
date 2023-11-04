package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.domain.entity.Track

interface TrackRepository {
    fun getTrack(): Track?
    fun putTrack(value: Track)
}