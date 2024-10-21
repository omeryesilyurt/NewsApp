package com.example.newsapp.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailBinding
import com.example.newsapp.model.NewsModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val news = arguments?.getSerializable("selectedNews") as? NewsModel
        binding.tvDetailNewsTitle.text = news?.name
        binding.tvDetailDescription.text = news?.description
        binding.imgDetailNews.setImageResource(R.drawable.news)
        if (news != null) {
            loadImage(news, binding.imgDetailNews)
        }
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigate(R.id.actionDetailFragmentToHomeFragment)
        }
        binding.toolbar.tvTitle.text = getText(R.string.title_home)
    }

    private fun loadImage(newsItem: NewsModel, imageView: AppCompatImageView) {
        val imageUrl = newsItem.image
        if (imageUrl != null) {
            if (imageUrl.isNotEmpty()) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.news)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.news)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
