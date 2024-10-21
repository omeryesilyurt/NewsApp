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

class FavoritesViewModel(private val localRepository: LocalRepository) :ViewModel(){

    val eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventShowProgress = MutableLiveData<Boolean>()
    private var favoriteList: List<NewsModel>? = null


    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            eventShowProgress.postValue(true)

            try {
                val response: Response<NewsResponseModel> = NetworkHelper.service.getNewsList("general")
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    localRepository.favoriteNewsList?.let { favoriteList ->
                        val filteredList = filterList(newsResponse?.result ?: emptyList(), favoriteList)
                        eventFetchNews.postValue(filteredList)
                    } ?: run {
                        eventFetchNews.postValue(null)
                    }
                } else {
                    eventFetchNews.postValue(null)
                }
            } catch (e: Exception) {
                eventFetchNews.postValue(null)
            } finally {
                eventShowProgress.postValue(false)
            }
        }
    }

    private fun filterList(newsList: List<NewsModel>, favorites: List<NewsModel>): List<NewsModel> {
        for (favoriteItem in favorites) {
            for (newsItem in newsList) {
                if (favoriteItem.id == newsItem.id) {
                    favoriteItem.isFavorite = true
                }
            }
        }
        return favorites
    }
}