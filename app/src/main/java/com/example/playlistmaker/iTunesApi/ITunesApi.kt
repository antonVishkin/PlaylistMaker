package com.example.playlistmaker.iTunesApi

import retrofit2.Call
import retrofit2.http.*

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String):Call<SearchResponse>
}