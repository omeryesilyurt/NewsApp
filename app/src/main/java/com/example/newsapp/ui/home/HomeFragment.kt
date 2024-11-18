package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.NewsModel
import com.example.newsapp.paging.NewsPagingAdapter
import com.example.newsapp.repository.LocalRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class HomeFragment : Fragment(), AddOrRemoveFavoriteListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private  val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter
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
        homeViewModel.fetchNews("general")


        adapter = HomeAdapter(newsList, { selectedNews ->
            if (selectedNews.newsId == null) {
                selectedNews.newsId = UUID.randomUUID()
            }
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            val bundle = Bundle().apply {
                putSerializable("selectedNews", selectedNews)
            }
            findNavController().navigate(action.actionId, bundle)
        }, this)
        binding.rvNews.adapter = adapter
        binding.toolbar.tvTitle.text = getText(R.string.title_home)

        binding.btnGeneral.setOnClickListener {
            highlightButton(
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "general"
            homeViewModel.fetchNews("general")
        }
        binding.btnSport.setOnClickListener {
            highlightButton(
                binding.btnSport,
                binding.btnGeneral,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "sport"
            homeViewModel.fetchNews("sport")
        }
        binding.btnEconomy.setOnClickListener {
            highlightButton(
                binding.btnEconomy,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnTechnology
            )
            selectedCategory = "economy"
            homeViewModel.fetchNews("economy")
        }
        binding.btnTechnology.setOnClickListener {
            highlightButton(
                binding.btnTechnology,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy
            )
            selectedCategory = "technology"
            homeViewModel.fetchNews("technology")
        }


    }

    private fun highlightButton(selectedButton: View, vararg otherButtons: View) {
        selectedButton.setBackgroundResource(R.drawable.selected_button_bg)
        otherButtons.forEach { it.setBackgroundResource(R.drawable.default_button_bg) }
    }


    override fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean) {
        homeViewModel.addOrRemove(news, isAdd)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.eventFetchNews.observe(viewLifecycleOwner) { newsList ->
            val mutableList: MutableList<NewsModel> = newsList?.toMutableList() ?: mutableListOf()
            adapter.updateData(mutableList)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
