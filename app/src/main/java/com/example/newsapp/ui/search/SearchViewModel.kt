package com.example.newsapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsModel
import com.example.newsapp.model.NewsResponseModel
import com.example.newsapp.network.ApiService
import com.example.newsapp.network.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews

    fun getData(query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<NewsResponseModel> =
                    apiService.getNewsList("general")
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    newsResponse?.let { newsList ->
                        val filteredList = if (query.isNullOrEmpty()) {
                            newsList.result
                        } else {
                            newsList.result.filter {
                                it.name?.contains(query, ignoreCase = true) == true
                            }
                        }
                        eventFetchNews.postValue(filteredList)
                    } ?: run {
                        eventFetchNews.postValue(null)
                    }
                }
            } catch (e: Exception) {
                eventFetchNews.postValue(null)
                println(e)
            }
        }
    }
}