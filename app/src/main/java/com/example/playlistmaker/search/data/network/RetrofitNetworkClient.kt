package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchRequest

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {


    override fun doRequest(dto: Any): Response {
        return if (dto is SearchRequest) {
            val response = iTunesService.search(dto.request).execute()
            val body = response.body() ?: Response()
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = CLIENT_ERROR_RESULT_CODE }
        }
    }

    companion object {
        const val CLIENT_ERROR_RESULT_CODE = 400
    }
}