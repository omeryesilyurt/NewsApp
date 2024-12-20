package com.example.newsapp.network

import com.example.newsapp.model.NewsResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("getNews?country=tr") // TODO dil bilgisini cihazdan al
    suspend fun getNewsList(
        @Query("tag") tag: String,
        @Query("paging") paging: Int
    ): Response<NewsResponseModel>
}