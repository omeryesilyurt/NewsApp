package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.NewsModel
import com.example.newsapp.paging.NewsPagingAdapter
import com.example.newsapp.repository.LocalRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class HomeFragment : Fragment(), AddOrRemoveFavoriteListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private  val homeViewModel: HomeViewModel by viewModels()
    //private lateinit var adapter: HomeAdapter
    private lateinit var pagingAdapter: NewsPagingAdapter
    private var newsList: MutableList<NewsModel> = mutableListOf()
    private var selectedCategory: String? = null


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
        pagingAdapter = NewsPagingAdapter()
        binding.rvNews.adapter = pagingAdapter
        val pagingAdapter = NewsPagingAdapter { newsItem ->
            findNavController().navigate(R.id.actionHomeFragmentToDetailFragment)
        }
        lifecycleScope.launch {
            homeViewModel.getNews("general").collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }


/*        adapter = HomeAdapter(newsList, { selectedNews ->
            if (selectedNews.newsId == null) {
                selectedNews.newsId = UUID.randomUUID()
            }
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            val bundle = Bundle().apply {
                putSerializable("selectedNews", selectedNews)
            }
            findNavController().navigate(action.actionId, bundle)
        }, this)*/
        binding.rvNews.adapter = pagingAdapter
        binding.toolbar.tvTitle.text = getText(R.string.title_home)

        binding.btnGeneral.setOnClickListener {
            highlightButton(
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "general"
            updateNews("general")
            homeViewModel.getNews("general")
        }
        binding.btnSport.setOnClickListener {
            highlightButton(
                binding.btnSport,
                binding.btnGeneral,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "sport"

            updateNews("sport")
            homeViewModel.getNews("sport")
        }
        binding.btnEconomy.setOnClickListener {
            highlightButton(
                binding.btnEconomy,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnTechnology
            )
            selectedCategory = "economy"
            updateNews("economy")
            homeViewModel.getNews("economy")
        }
        binding.btnTechnology.setOnClickListener {
            highlightButton(
                binding.btnTechnology,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy
            )
            selectedCategory = "technology"
            updateNews("technology")
            homeViewModel.getNews("technology")
        }

    }

    private fun highlightButton(selectedButton: View, vararg otherButtons: View) {
        selectedButton.setBackgroundResource(R.drawable.selected_button_bg)
        otherButtons.forEach { it.setBackgroundResource(R.drawable.default_button_bg) }
    }

    private fun updateNews(category: String) {
        lifecycleScope.launch {
            homeViewModel.getNews(category).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }}


    override fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean) {
        homeViewModel.addOrRemove(news, isAdd)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            homeViewModel.getNews("general").collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
