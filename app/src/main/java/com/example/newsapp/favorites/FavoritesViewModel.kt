package com.example.newsapp.favorites

import android.widget.Toast
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

    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews
    private val eventShowProgress = MutableLiveData<Boolean>()


    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            eventShowProgress.postValue(true)
            try {
                val response: Response<NewsResponseModel> = NetworkHelper.service.getNewsList("general")
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
}