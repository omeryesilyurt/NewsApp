package com.example.newsapp.ui.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.ApiService
import com.example.newsapp.network.NetworkHelper
import com.example.newsapp.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val apiService: ApiService
) :
    ViewModel() {
    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews


    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val favNews = localRepository.getFavoriteList()
            val response = apiService.getNewsList("general")
            response.body()?.let {
                if (it.success) {
                    val list = it.result
                    if (favNews != null) {
                        for (i in list) {
                            val matchedNews = favNews.any { i.name == it.name }
                            if (matchedNews) {
                                i.isFavorite = true
                            }
                        }
                    }
                    _eventFetchNews.postValue(list)
                }
            }
        }
    }

    fun fetchSportNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.getNewsList("sport")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun fetchEconomyNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =apiService.getNewsList("economy")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun fetchTechnologyNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.getNewsList("technology")
            response.body()?.let {
                if (it.success)
                    eventFetchNews.postValue(it.result)
            }
        }
    }

    fun addOrRemove(news: NewsModel, isAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (news.newsId == null) {
                news.newsId = UUID.randomUUID()
            }
            if (isAdd) {
                news.isFavorite = true
                localRepository.add(news)
            } else {
                news.isFavorite = false
                localRepository.remove(news)
            }
        }
    }

}