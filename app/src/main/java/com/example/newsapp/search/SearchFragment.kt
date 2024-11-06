package com.example.newsapp.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.model.NewsModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.tvTitle.text = getText(R.string.title_search)
        adapter = SearchAdapter()
        binding.rvSearchNews.adapter = adapter
        searchViewModel.eventFetchNews.observe(viewLifecycleOwner) { filteredList ->
            if (filteredList != null) {
                adapter.updateList(filteredList)
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.getData(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.getData(newText)
                return true
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
