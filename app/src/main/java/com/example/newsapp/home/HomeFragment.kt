package com.example.newsapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.NewsModel
import com.example.newsapp.repository.LocalRepository

class HomeFragment : Fragment(), AddOrRemoveFavoriteListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private var newsList: MutableList<NewsModel> = mutableListOf()
    private var isNewsFetched = false
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
        val localRepository = LocalRepository(requireContext())
        val factory = HomeViewModelFactory(localRepository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        when (selectedCategory) {
            "general" -> highlightButton(
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy,
                binding.btnTechnology
            )

            "sport" -> highlightButton(
                binding.btnSport,
                binding.btnGeneral,
                binding.btnEconomy,
                binding.btnTechnology
            )

            "economy" -> highlightButton(
                binding.btnEconomy,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnTechnology
            )

            "technology" -> highlightButton(
                binding.btnTechnology,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy
            )

            else -> highlightButton(
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy,
                binding.btnTechnology
            )
        }

        adapter = HomeAdapter(newsList, { selectedNews ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            val bundle = Bundle().apply {
                putSerializable("selectedNews", selectedNews)
            }
            findNavController().navigate(action.actionId, bundle)
        }, this)
        binding.rvNews.adapter = adapter
        binding.toolbar.tvTitle.text = getText(R.string.title_home)
        viewModel.eventFetchNews.observe(viewLifecycleOwner) { newsList ->
            val mutableList: MutableList<NewsModel> = newsList?.toMutableList() ?: mutableListOf()
            adapter.updateData(mutableList)
        }

        if (!isNewsFetched) {
            viewModel.fetchNews()
            isNewsFetched = true
        }

        binding.btnGeneral.setOnClickListener {
            highlightButton(
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "general"
            viewModel.fetchNews()
        }
        binding.btnSport.setOnClickListener {
            highlightButton(
                binding.btnSport,
                binding.btnGeneral,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "sport"
            viewModel.fetchSportNews()
        }
        binding.btnEconomy.setOnClickListener {
            highlightButton(
                binding.btnEconomy,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnTechnology
            )
            selectedCategory = "economy"
            viewModel.fetchEconomyNews()
        }
        binding.btnTechnology.setOnClickListener {
            highlightButton(
                binding.btnTechnology,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy
            )
            selectedCategory = "technology"
            viewModel.fetchTechnologyNews()
        }


    }

    private fun highlightButton(selectedButton: View, vararg otherButtons: View) {
        selectedButton.setBackgroundResource(R.drawable.selected_button_bg)
        otherButtons.forEach { it.setBackgroundResource(R.drawable.default_button_bg) }
    }


    override fun onAddOrRemoveFavorite(news: NewsModel, isAdd: Boolean) {
        viewModel.addOrRemove(news, isAdd)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
