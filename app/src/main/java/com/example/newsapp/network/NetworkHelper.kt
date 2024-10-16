package com.example.newsapp.network

import com.example.newsapp.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class NetworkHelper {
    companion object {

        val service = getRetrofitInstance().create(ApiService::class.java)


        private fun getRetrofitInstance(): Retrofit {
            val gson = GsonBuilder().setLenient().create()
            val gsonConverterFactory = GsonConverterFactory.create(gson)

            val httpClientBuilder = OkHttpClient.Builder()
            val httpClient = httpClientBuilder.addInterceptor { chain ->
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .build()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url).addHeader("authorization",Constants.API_KEY).addHeader("content-type","application/json")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }.build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(httpClient).build()
        }
    }
}