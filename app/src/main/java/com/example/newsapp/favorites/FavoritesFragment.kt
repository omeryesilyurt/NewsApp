package com.example.newsapp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.model.NewsModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: FavoritesAdapter
    private lateinit var favoriteListAdapter: FavoritesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    //TODO Lateinit propertyler tanımlanacak.
    //TODO ViewModelFactory gerekiyor.
    //TODO Favori butonu burada da aktif edilecek


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventFetchNews.observe(viewLifecycleOwner , handleFetchNews)
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
