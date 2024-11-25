package com.example.newsapp.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.model.NewsModel
import com.example.newsapp.model.NewsResponseModel
import com.example.newsapp.network.ApiService
import com.example.newsapp.paging.NewsPagingAdapter
import com.example.newsapp.paging.NewsPagingSource
import com.example.newsapp.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale.Category
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val apiService: ApiService
) : ViewModel() {


    private val _eventFetchNews = MutableLiveData<List<NewsModel>?>()
    val eventFetchNews: MutableLiveData<List<NewsModel>?> get() = _eventFetchNews
    private lateinit var favoriteListAdapter: NewsPagingAdapter

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = listOf("general", "sport", "economy", "technology")
                val allNews = mutableListOf<NewsModel>()

                for (category in categories) {
                    val response: Response<NewsResponseModel> = apiService.getNewsList(category,20)
                    if (response.isSuccessful) {
                        val newsResponse = response.body()
                        newsResponse?.let { newsList ->
                            allNews.addAll(newsList.result)
                        }
                    }
                }
                if (allNews.isEmpty()) {
                    eventFetchNews.postValue(null)
                } else {
                    val updatedNewsList = localRepository.getFavoriteNews(allNews)
                    eventFetchNews.postValue(updatedNewsList)
                }

            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun getFavoriteNews(): Flow<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NewsPagingSource(apiService, category = "favorites",localRepository,isFavoritesMode = true) }
        ).flow.cachedIn(viewModelScope)
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
        _eventFetchNews.postValue(localRepository.getFavoriteList())
    }
}