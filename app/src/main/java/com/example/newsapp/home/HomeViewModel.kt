package com.example.newsapp.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.NetworkHelper
import com.example.newsapp.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val localRepository: LocalRepository ) :
    ViewModel(){
    val eventFetchNews = MutableLiveData<List<NewsModel>>()
    val eventShowProgress = MutableLiveData<Boolean>()
    private var favoriteList: List<NewsModel>? = null


    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkHelper.service.getNewsList("general")
            response.body()?.let {
                if (it.success) {
                    eventFetchNews.postValue(it.result)
                }
            }
        }
    }

    fun fetchSportNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkHelper.service.getNewsList("sport")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun fetchEconomyNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkHelper.service.getNewsList("economy")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun fetchTechnologyNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkHelper.service.getNewsList("technology")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun addOrRemove(news: NewsModel, isAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isAdd) {
                news.isFavorite = true
                localRepository.add(news)
            } else
                news.isFavorite = false
                localRepository.remove(news)
        }
    }

    private fun filterList(newsList: List<NewsModel>): List<NewsModel> {
        if (favoriteList != null) {
            for (favoriteItem in favoriteList!!) {
                for (newsItem in newsList) {
                    if (favoriteItem.id == newsItem.id) {
                        newsItem.isFavorite = true
                    }
                }
            }
        }
        return newsList
    }
}