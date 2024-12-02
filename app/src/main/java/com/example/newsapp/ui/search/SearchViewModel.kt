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

    fun searchPagingData(query: String?,category: String): LiveData<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NewsPagingSource(apiService, category, localRepository, query) }
        ).liveData.cachedIn(viewModelScope)
    }

}