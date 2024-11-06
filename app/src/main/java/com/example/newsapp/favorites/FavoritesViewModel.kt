package com.example.newsapp.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.model.NewsResponseModel
import com.example.newsapp.network.NetworkHelper
import com.example.newsapp.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.UUID

class FavoritesViewModel(private val localRepository: LocalRepository) : ViewModel() {

    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<NewsResponseModel> =
                    NetworkHelper.service.getNewsList("general")
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    newsResponse?.let { newsList ->
                        val updatedNewsList = localRepository.getFavoriteNews(newsList.result)
                        eventFetchNews.postValue(updatedNewsList)
                    } ?: run {
                        eventFetchNews.postValue(null)
                    }
                }
            } catch (e: Exception) {
                println(e)
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
            _eventFetchNews.postValue(localRepository.getFavoriteList())
        }
    }
}