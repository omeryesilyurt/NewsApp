package com.example.newsapp.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val eventFetchNews = MutableLiveData<List<NewsModel>>()
    val eventShowProgress = MutableLiveData<Boolean>()

    fun fetchNews() {
            viewModelScope.launch(Dispatchers.IO){
                val response = NetworkHelper.service.getNewsList("general")
                response.body()?.let {
                    if (it.success) {
                        eventFetchNews.postValue(it.result)
                    }
                }
            }
    }

    fun fetchSportNews() {
        viewModelScope.launch(Dispatchers.IO){
            val response = NetworkHelper.service.getNewsList("sport")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun fetchEconomyNews() {
        viewModelScope.launch (Dispatchers.IO){
            val response = NetworkHelper.service.getNewsList("economy")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun fetchTechnologyNews() {
        viewModelScope.launch (Dispatchers.IO){
            val response = NetworkHelper.service.getNewsList("technology")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }
}