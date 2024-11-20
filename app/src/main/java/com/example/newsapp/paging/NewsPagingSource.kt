package com.example.newsapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.ApiService

class NewsPagingSource(private val apiService: ApiService, private val category: String) :
    PagingSource<Int, NewsModel>() {
    override fun getRefreshKey(state: PagingState<Int, NewsModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition)
        return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsModel> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getNewsList(tag = category, paging = page)
            val newsList = response.body()?.result ?: emptyList()
            val data = response.body()?.result ?: emptyList()
            Log.d("PagingDebug", "Gelen veri boyutu: ${data.size}")
            LoadResult.Page(
                data = newsList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (newsList.isEmpty() || newsList.size < PAGINATION_LIMIT) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    companion object {
        const val PAGINATION_LIMIT = 4
    }
}

