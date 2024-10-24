package com.example.newsapp.repository

import android.content.Context
import com.example.newsapp.database.NewsDao
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.model.NewsModel

class LocalRepository(context: Context) {
    private val newsDB = NewsDatabase.getInstance(context)

    fun add(news: NewsModel) {
        newsDB?.NewsDao()?.addFavorite(news)
    }

    fun remove(news: NewsModel) {
        newsDB?.NewsDao()?.removeFavorite(news.id)
    }

    fun getFavoriteList():List<NewsModel>? {
       return newsDB?.NewsDao()?.getAllFavoriteNews()
    }

    fun getFavoriteNews(newsList: List<NewsModel>): List<NewsModel> {
        val favoriteNews = newsDB?.NewsDao()?.getAllFavoriteNews()
        return if (favoriteNews != null) {
            newsList.filter { newsItem ->
                favoriteNews.any { it.key == newsItem.key }
            }
        } else {
            emptyList() // Favori haberler boşsa boş bir liste döner
        }
    }
}