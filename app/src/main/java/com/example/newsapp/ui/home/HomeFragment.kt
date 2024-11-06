package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.NewsModel
import com.example.newsapp.repository.LocalRepository
import java.util.UUID

class HomeFragment : Fragment(), AddOrRemoveFavoriteListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
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
        val localRepository = LocalRepository(requireContext())
        val homeViewModelFactory = HomeViewModelFactory(localRepository)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        homeViewModel.fetchNews()


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
            if (selectedNews.newsId == null) {
                selectedNews.newsId = UUID.randomUUID()
            }
            val action = com.example.newsapp.home.HomeFragmentDirections.actionHomeFragmentToDetailFragment()
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
            homeViewModel.fetchNews()
        }
        binding.btnSport.setOnClickListener {
            highlightButton(
                binding.btnSport,
                binding.btnGeneral,
                binding.btnEconomy,
                binding.btnTechnology
            )
            selectedCategory = "sport"
            homeViewModel.fetchSportNews()
        }
        binding.btnEconomy.setOnClickListener {
            highlightButton(
                binding.btnEconomy,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnTechnology
            )
            selectedCategory = "economy"
            homeViewModel.fetchEconomyNews()
        }
        binding.btnTechnology.setOnClickListener {
            highlightButton(
                binding.btnTechnology,
                binding.btnGeneral,
                binding.btnSport,
                binding.btnEconomy
            )
            selectedCategory = "technology"
            homeViewModel.fetchTechnologyNews()
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
