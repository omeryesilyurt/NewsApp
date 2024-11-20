package com.example.newsapp.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsPagingRepository @Inject constructor (private val apiService: ApiService) {
    fun getNewsPagingData(category: String): Flow<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NewsPagingSource.PAGINATION_LIMIT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(apiService, category) }
        ).flow
    }
}