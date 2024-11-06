package com.example.newsapp.ui.home

import com.example.newsapp.model.NewsModel

interface AddOrRemoveFavoriteListener {

    fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean)
}