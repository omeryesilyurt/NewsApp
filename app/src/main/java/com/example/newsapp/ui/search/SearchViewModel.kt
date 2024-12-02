package com.example.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.newsapp.model.NewsModel
import com.example.newsapp.model.NewsResponseModel
import com.example.newsapp.network.ApiService
import com.example.newsapp.paging.NewsPagingSource
import com.example.newsapp.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apiService: ApiService,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews

    fun getData(query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<NewsResponseModel> =
                    apiService.getNewsList("general", 20)
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

    fun searchPagingData(query: String?,category: String): LiveData<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NewsPagingSource(apiService, category, localRepository, query) }
        ).liveData.cachedIn(viewModelScope)
    }

}