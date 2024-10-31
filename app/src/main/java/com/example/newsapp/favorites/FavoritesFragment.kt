package com.example.newsapp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.home.AddOrRemoveFavoriteListener
import com.example.newsapp.model.NewsModel
import com.example.newsapp.repository.LocalRepository

class FavoritesFragment : Fragment(), AddOrRemoveFavoriteListener {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var favoriteListAdapter: FavoritesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val localRepository = LocalRepository(requireContext())
        val factory = FavoritesViewModelFactory(localRepository)
        viewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
        favoriteListAdapter = FavoritesAdapter(mutableListOf(), this)
        binding.toolbar.tvTitle.text = getText(R.string.title_fav)
        binding.rvFavNews.adapter = favoriteListAdapter
        viewModel.eventFetchNews.observe(viewLifecycleOwner) { newsList ->
            val mutableList: MutableList<NewsModel>? = newsList?.toMutableList()
            favoriteListAdapter.updateList(mutableList)
        }
    }

    private val handleFetchNews = Observer<List<NewsModel>?> {
        favoriteListAdapter.updateList(it)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
        viewModel.eventFetchNews.observe(viewLifecycleOwner, handleFetchNews)
    }

    override fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean) {
        viewModel.addOrRemove(news, isAdd)
    }
}
