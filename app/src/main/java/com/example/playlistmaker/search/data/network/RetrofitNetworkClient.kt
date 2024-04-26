package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {


    override suspend fun doRequest(dto: Any): Response {
        if (dto !is SearchRequest){
            return Response().apply { resultCode = CLIENT_ERROR_RESULT_CODE }
        }
        return withContext(Dispatchers.IO){
            try {
            val response = iTunesService.search(dto.request)
            response.apply { resultCode = CLIENT_SUCCESS_RESULT_CODE }
        } catch (e:Throwable) {
            Response().apply { resultCode = CLIENT_ERROR_RESULT_CODE }
        }}
    }

    companion object {
        const val CLIENT_ERROR_RESULT_CODE = 400
        const val CLIENT_SUCCESS_RESULT_CODE = 200
    }
}