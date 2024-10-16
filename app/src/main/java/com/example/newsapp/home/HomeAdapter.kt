package com.example.newsapp.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.NewsModel

class HomeAdapter(private var itemList: List<NewsModel>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: AppCompatTextView = itemView.findViewById(R.id.tvNewsTitle)
        val imageView: AppCompatImageView = itemView.findViewById(R.id.imgNews)
        val textViewSource: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val textViewDescription: AppCompatTextView = itemView.findViewById(R.id.tvDescription)
        // val favoriteButton: AppCompatImageButton = itemView.findViewById(R.id.btnFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = itemList[position].name
        holder.textViewSource.text = itemList[position].source
        holder.textViewDescription.text = itemList[position].description
        holder.imageView.setImageResource(R.drawable.news) // TODO şimdilik local görsel kullanıldı daha sonra servisten gelen görsel kullanılacak
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newsList: List<NewsModel>) {
        itemList = newsList
        notifyDataSetChanged()
    }
}