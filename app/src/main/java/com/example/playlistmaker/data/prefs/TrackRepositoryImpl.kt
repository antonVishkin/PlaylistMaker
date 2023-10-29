package com.example.playlistmaker.data.prefs

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.entity.Track
import com.google.gson.Gson

class TrackRepositoryImpl : TrackRepository {
    private lateinit var sharedPreference: SharedPreferences
    override fun init(context: Context) {
        sharedPreference = context.getSharedPreferences(
            PLAYLIST_MAKER_SHARED_PREFERENCES,
            Application.MODE_PRIVATE
        )
    }

    override fun getTrack(): Track? {
        val json = sharedPreference.getString(TRACK_DATA, "")
        return Gson().fromJson(json, Track::class.java)
    }

    override fun putTrack(value: Track) {
        sharedPreference.edit().putString(TRACK_DATA, Gson().toJson(value)).apply()
    }

    companion object {
        const val PLAYLIST_MAKER_SHARED_PREFERENCES = "playlist_maker_shared_preferences"
        const val TRACK_DATA = "TRACK_DATA"
    }
}