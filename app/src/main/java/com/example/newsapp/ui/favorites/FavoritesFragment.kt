package com.example.newsapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.home.AddOrRemoveFavoriteListener
import com.example.newsapp.model.NewsModel
import com.example.newsapp.repository.LocalRepository

class FavoritesFragment : Fragment(), AddOrRemoveFavoriteListener {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteViewModel: FavoritesViewModel
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
        val favoritesViewModelFactory = FavoritesViewModelFactory(localRepository)
        favoriteViewModel = ViewModelProvider(this, favoritesViewModelFactory)[FavoritesViewModel::class.java]
        favoriteListAdapter = FavoritesAdapter(mutableListOf(), this)
        binding.toolbar.tvTitle.text = getText(R.string.title_fav)
        binding.rvFavNews.adapter = favoriteListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.eventFetchNews.observe(viewLifecycleOwner) { newsList ->
            val mutableList: MutableList<NewsModel>? = newsList?.toMutableList()
            favoriteListAdapter.updateList(mutableList)
        }
        favoriteViewModel.getData()
    }

    override fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean) {
        favoriteViewModel.addOrRemove(news, isAdd)
    }
}
