package com.example.newsapp.repository

import android.content.Context
import com.example.newsapp.database.NewsDao
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.model.NewsModel

class LocalRepository(context: Context) {
    private val newsDB = NewsDatabase.getInstance(context)

    val favoriteNewsList: List<NewsModel>? = newsDB?.NewsDao()?.getAllFavoriteNews()

    fun add(news: NewsModel) {
        newsDB?.NewsDao()?.addFavorite(news)
    }

    fun remove(news: NewsModel) {
        newsDB?.NewsDao()?.removeFavorite(news.id)
    }

    fun getFavoriteList(news: NewsModel) {
        newsDB?.NewsDao()?.getAllFavoriteNews()
    }
}