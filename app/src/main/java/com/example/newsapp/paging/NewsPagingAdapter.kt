package com.example.newsapp.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.NewsModel
import java.util.Locale

class NewsPagingAdapter(
    private val onItemClick: ((NewsModel) -> Unit)? = null,
    private val addOrRemoveFavoriteListener: ((NewsModel, Boolean) -> Unit)? = null,
    private var fullList: MutableList<NewsModel>? = null,
    private var itemList: MutableList<NewsModel>? = null

) :
    PagingDataAdapter<NewsModel, NewsPagingAdapter.NewsViewHolder>(NewsDiffCallback()), Filterable {

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val onItemClick: ((NewsModel) -> Unit)? = null,
        private val addOrRemoveFavoriteListener: ((NewsModel, Boolean) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsModel?) {
            news?.let {
                binding.apply {
                    tvNewsTitle.text = it.name
                    tvTitle.text = it.source
                    tvDescription.text = it.description
                    loadImage(it, imgNews)
                    itemView.setOnClickListener {
                        onItemClick?.let { it1 -> it1(news) }
                    }

                    if (it.isFavorite == true) {
                        btnFav.setImageResource(R.drawable.ic_mark_checked)
                    } else {
                        btnFav.setImageResource(R.drawable.ic_mark_unchecked)
                    }

                    btnFav.setOnClickListener {
                        val newFavoriteStatus = !(news.isFavorite ?: false)
                        if (newFavoriteStatus) {
                            btnFav.setImageResource(R.drawable.ic_mark_checked)
                        } else {
                            btnFav.setImageResource(R.drawable.ic_mark_unchecked)
                        }
                        addOrRemoveFavoriteListener?.let { it1 -> it1(news, newFavoriteStatus) }
                    }
                }
            }
        }

        private fun loadImage(newsItem: NewsModel, imageView: ImageView) {
            if (!newsItem.image.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(newsItem.image)
                    .placeholder(R.drawable.news)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewsViewHolder(binding, onItemClick, addOrRemoveFavoriteListener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsModel>() {
        override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem == newItem
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSearchList(newList: List<NewsModel>) {
        fullList?.clear()
        fullList?.addAll(newList)
        itemList?.clear()
        itemList?.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullList
                } else {
                    val query = constraint.toString().lowercase(Locale.getDefault()).trim()
                    fullList?.filter { news ->
                        (news.name?.lowercase(Locale.getDefault())?.contains(query) == true)
                    }?.toMutableList()
                }
                return FilterResults().apply { values = filteredList }
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemList?.clear()
                if (results?.values != null) {
                    itemList?.addAll(results.values as List<NewsModel>)
                }
                notifyDataSetChanged()
            }

        }
    }
}