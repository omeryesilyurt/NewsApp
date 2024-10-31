package com.example.newsapp.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.NetworkHelper
import com.example.newsapp.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val localRepository: LocalRepository ) :
    ViewModel(){
    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews
    val eventShowProgress = MutableLiveData<Boolean>()


    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val favNews = localRepository.getFavoriteList()
            val response = NetworkHelper.service.getNewsList("general")
            response.body()?.let {
                if (it.success) {
                   val list = it.result
                    if (favNews != null) {
                        for (i in list) {
                            val match2 = favNews.any { i.newsId == it.newsId}
                            Log.i("newsId:","$it.newsId")
                            if (match2) {
                                i.isFavorite = true
                            }
                        }
                    }
                    //TODO kaydetme işlemini id ile yapmak gerekiyor fakat id auto generate olmuyor sabit 0 kalıyor.
                    _eventFetchNews.postValue(list)
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

    fun addOrRemove( news: NewsModel, isAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isAdd) {
                news.isFavorite = true
                localRepository.add(news)
            } else{
                news.isFavorite = false
                localRepository.remove(news)
            }
        }
    }

}