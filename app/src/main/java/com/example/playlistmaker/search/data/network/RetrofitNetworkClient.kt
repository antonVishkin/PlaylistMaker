package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit =
        Retrofit.Builder().baseUrl(SearchActivity.ITUNES_BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            val response = iTunesService.search(dto.request).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = CLIENT_ERROR_RESULT_CODE }
        }
    }
    companion object{
        const val CLIENT_ERROR_RESULT_CODE = 400
    }
}