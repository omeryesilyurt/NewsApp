package com.example.newsapp.network

import com.example.newsapp.model.NewsResponseModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("getNews?country=tr")
    suspend fun getNewsList(@Query("tag") tag : String): Response<NewsResponseModel>
}