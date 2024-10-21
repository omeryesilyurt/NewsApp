package com.example.newsapp.home

import com.example.newsapp.model.NewsModel

interface AddOrRemoveFavoriteListener {

    fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean)
}