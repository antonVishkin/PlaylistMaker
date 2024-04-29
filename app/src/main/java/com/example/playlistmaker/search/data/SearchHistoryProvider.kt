package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryProvider(
    private val gson: Gson,
    private val prefs: SharedPreferences,
    private val appDatabase: AppDatabase,
) : SearchHistoryRepository {
    private lateinit var searchList: ArrayList<Track>
    private val limit = 10


    override fun getHistory(): Flow<List<Track>> = flow {
        if (!::searchList.isInitialized) {
            val listAsString = prefs.getString(SEARCH_HISTORY_KEY, "")
            val itemType = object : TypeToken<ArrayList<Track>>() {}.type
            searchList = gson.fromJson<ArrayList<Track>>(listAsString, itemType) ?: arrayListOf()
        }
        removeOverLimited()
        val favoritesIdList = appDatabase.trackDao().getFavoritesIdList()
        emit(searchList.onEach {
            if (favoritesIdList.contains(it.trackId))
                it.isFavorite = true
        })
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

    companion object {
        const val SEARCH_HISTORY_KEY = "search_history"
    }
}