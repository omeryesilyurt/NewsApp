package com.example.newsapp.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.NetworkHelper
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val eventFetchNews = MutableLiveData<List<NewsModel>>()
    val eventShowProgress = MutableLiveData<Boolean>()

    fun fetchNews() {
        viewModelScope.launch {
            val response = NetworkHelper.service.getNewsList("general")
            response.body()?.let {
                if (it.success){
                    eventFetchNews.postValue(it.result)
                }
            }
        }
    }
}