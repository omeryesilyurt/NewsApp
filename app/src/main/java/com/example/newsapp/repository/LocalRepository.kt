package com.example.newsapp.repository

import android.content.Context
import com.example.newsapp.database.NewsDao
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.model.NewsModel

class LocalRepository(context: Context) {
    private val newsDB = NewsDatabase.getInstance(context)

    suspend fun add(news: NewsModel) {
        newsDB?.NewsDao()?.addFavorite(news)
    }

    suspend fun remove(news: NewsModel) {
        news.name?.let { newsDB?.NewsDao()?.removeFavorite(it) }
    }

    suspend fun getFavoriteList(): List<NewsModel>? {
        return newsDB?.NewsDao()?.getAllFavoriteNews()
    }

    suspend fun getFavoriteNews(newsList: List<NewsModel>): List<NewsModel> {
        val favoriteNews = newsDB?.NewsDao()?.getAllFavoriteNews()
        return if (favoriteNews != null) {
            newsList.filter { newsItem ->
                favoriteNews.any { it.name == newsItem.name }
            }
        } else {
            emptyList()
        }
    }
}