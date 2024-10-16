package com.example.newsapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.NewsModel
import com.example.newsapp.network.NetworkHelper
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: HomeAdapter
    private var newsList: MutableList<NewsModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter(newsList)
        binding.rvNews.adapter = adapter
        binding.toolbar.tvTitle.text = getText(R.string.title_home)
        viewModel.eventFetchNews.observe(viewLifecycleOwner,handleFetchData)
        viewModel.fetchNews()
    }


    private val handleFetchData = Observer<List<NewsModel>> {
        adapter.updateData(it)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
