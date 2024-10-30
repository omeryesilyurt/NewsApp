package com.example.newsapp.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.home.AddOrRemoveFavoriteListener
import com.example.newsapp.model.NewsModel

class FavoritesAdapter(
    private var itemList: MutableList<NewsModel> = mutableListOf(),
    private val addOrRemoveFavoriteListener: AddOrRemoveFavoriteListener
) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
    private lateinit var newsDatabase: NewsDatabase

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: AppCompatTextView = itemView.findViewById(R.id.tvNewsTitle)
        val imageView: AppCompatImageView = itemView.findViewById(R.id.imgNews)
        val textViewSource: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val textViewDescription: AppCompatTextView = itemView.findViewById(R.id.tvDescription)
        val favoriteButton: AppCompatImageButton = itemView.findViewById(R.id.btnFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        newsDatabase = NewsDatabase.getInstance(parent.context)!!
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = itemList[position].name
        holder.textViewSource.text = itemList[position].source
        holder.textViewDescription.text = itemList[position].description
        loadImage(itemList[position] ,holder.imageView)
        holder.favoriteButton.setImageResource(R.drawable.ic_mark_checked)
        holder.favoriteButton.setOnClickListener {
            holder.favoriteButton.setImageResource(R.drawable.ic_mark_unchecked)
            addOrRemoveFavoriteListener.onAddOrRemoveFavorite(itemList[position], false)
            notifyItemChanged(position)
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(itemList: List<NewsModel>?) {
        this.itemList = itemList?.toMutableList() ?: mutableListOf()
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}