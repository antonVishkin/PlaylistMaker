package com.example.playlistmaker.search.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.PLAYLIST_MAKER_SHARED_PREFERENCES
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryProvider(context: Context):SearchHistoryRepository {
    private lateinit var searchList: ArrayList<Track>
    private val limit = 10
    private val prefs = context.getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    override fun getHistory(): ArrayList<Track> {
        if (!::searchList.isInitialized) {
            val listAsString = prefs.getString(SEARCH_HISTORY_KEY, "")
            val itemType = object : TypeToken<ArrayList<Track>>() {}.type
            searchList = Gson().fromJson<ArrayList<Track>>(listAsString, itemType) ?: arrayListOf()
        }
        removeOverLimited()
        return searchList
    }

    override fun addTrackToHistory(track: Track) {
        if (!::searchList.isInitialized) getHistory()
        if (searchList.contains(track))
            searchList.remove(track)
        if (searchList.size == limit)
            searchList.removeLast()
        searchList.add(0, track)
        prefs.edit().putString(SEARCH_HISTORY_KEY, Gson().toJson(searchList)).apply()
    }

    override fun clearHistory() {
        searchList.clear()
        prefs.edit().putString(SEARCH_HISTORY_KEY, "").apply()
    }

    fun getSize(): Int {
        if (!::searchList.isInitialized) getHistory()
        return searchList.size
    }

    private fun removeOverLimited() {
        searchList = ArrayList(searchList.take(limit))
    }

    companion object{
        const val SEARCH_HISTORY_KEY = "search_history"
    }
}