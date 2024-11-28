package com.example.newsapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.home.AddOrRemoveFavoriteListener
import com.example.newsapp.model.NewsModel
import com.example.newsapp.paging.NewsPagingAdapter
import com.example.newsapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseFragment(), AddOrRemoveFavoriteListener {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoritesViewModel by viewModels()
    private lateinit var favoriteListAdapter: NewsPagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    //TODO: Favoriden çıkarınca çıkıyor ama liste güncellenmiyor.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteListAdapter =
            NewsPagingAdapter(addOrRemoveFavoriteListener = { newsItem, isFavorite ->
                favoriteViewModel.addOrRemove(newsItem, isFavorite)
            })
        binding.toolbar.tvTitle.text = getText(R.string.title_fav)
        binding.rvFavNews.adapter = favoriteListAdapter
        getFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean) {
        favoriteViewModel.addOrRemove(news, isAdd)
    }

    private fun getFavorites(){
        lifecycleScope.launch {
            favoriteViewModel.getFavoriteNews().collectLatest { pagingData ->
                favoriteListAdapter.submitData(pagingData)
            }
        }
    }
}
