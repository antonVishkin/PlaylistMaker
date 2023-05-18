package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.SearchActivity.Companion.SEARCH_HISTORY_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryProvider (val prefs:SharedPreferences){
    private lateinit var searchList:ArrayList<Track>
    fun getSearchHistory():ArrayList<Track>{
        if (!::searchList.isInitialized){
        val listAsString = prefs.getString(SEARCH_HISTORY_KEY, "")
        val itemType = object : TypeToken<ArrayList<Track>>(){}.type
        searchList = Gson().fromJson<ArrayList<Track>>(listAsString, itemType)?: arrayListOf()
        }
        return  searchList
    }
    fun addTrackToHistory(track: Track){
        if (!::searchList.isInitialized) getSearchHistory()
        if (searchList.contains(track)){
            searchList.remove(track)
            searchList.add(0,track)
        } else {
            searchList.add(track)
        }
        prefs.edit().putString(SEARCH_HISTORY_KEY, Gson().toJson(searchList)).apply()
    }
    fun clearHistory(){
        searchList.clear()
        prefs.edit().putString(SEARCH_HISTORY_KEY,"").apply()
    }
    fun getSize():Int{
        if (!::searchList.isInitialized) getSearchHistory()
        return searchList.size
    }
}