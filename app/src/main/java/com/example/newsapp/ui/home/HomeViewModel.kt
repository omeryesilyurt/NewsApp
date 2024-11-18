package com.example.newsapp.ui.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.ApiService
import com.example.newsapp.paging.NewsPagingSource
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

    fun fetchNews(category: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favNews = localRepository.getFavoriteList()
                val categories = category?.let { listOf(it) } ?: listOf(
                    "general",
                    "sport",
                    "economy",
                    "technology"
                )
                val allNews = mutableListOf<NewsModel>()

                for (cat in categories) {
                    val response = apiService.getNewsList(cat)
                    response.body()?.let {
                        if (it.success) {
                            allNews.addAll(it.result)
                        }
                    }
                }

                if (favNews != null) {
                    for (news in allNews) {
                        news.isFavorite = favNews.any { it.name == news.name }
                    }
                }

                _eventFetchNews.postValue(allNews)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    val pagedNews = Pager(
        config = PagingConfig(
            pageSize = NewsPagingSource.PAGINATION_LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewsPagingSource(apiService, "general") }
    ).flow.cachedIn(viewModelScope)

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