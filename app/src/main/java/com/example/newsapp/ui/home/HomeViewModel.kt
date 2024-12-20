package com.example.newsapp.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.ApiService
import com.example.newsapp.paging.NewsPagingSource
import com.example.newsapp.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val apiService: ApiService,
) :
    ViewModel() {
    var selectedCategory: String? = null


    fun getNews(category: String): Flow<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                NewsPagingSource(
                    apiService,
                    category,
                    localRepository,
                    isFavoritesMode = false
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun addOrRemove(news: NewsModel, isAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (news.newsId == null) {
                news.newsId = UUID.randomUUID()
            }
            if (isAdd) {
                news.isFavorite = true
                news.addedAt = System.currentTimeMillis()
                localRepository.add(news)
            } else {
                news.isFavorite = false
                localRepository.remove(news)
            }
        }
    }

}