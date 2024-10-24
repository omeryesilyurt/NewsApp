package com.example.newsapp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.home.HomeViewModel
import com.example.newsapp.home.HomeViewModelFactory
import com.example.newsapp.model.NewsModel
import com.example.newsapp.repository.LocalRepository

class FavoritesFragment : Fragment() {
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

    //TODO Favori butonu burada da aktif edilecek.
    //TODO Burada listelenen bütün itemlerin favorisi sorgusuz true olarak işaretlenecek


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val localRepository = LocalRepository(requireContext())
        val factory = FavoritesViewModelFactory(localRepository)
        viewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
        favoriteListAdapter = FavoritesAdapter(mutableListOf())
        viewModel.eventFetchNews.observe(viewLifecycleOwner, handleFetchNews)
        binding.toolbar.tvTitle.text = getText(R.string.title_fav)
        binding.rvFavNews.adapter = favoriteListAdapter
        viewModel.getData()
    }

    private val handleFetchNews = Observer<List<NewsModel>?> {
        favoriteListAdapter.updateList(it)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
