package com.example.newsapp.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.NewsModel
import java.util.Locale

class SearchAdapter(
    private var itemList: MutableList<NewsModel> = mutableListOf(),
    private var fullList: MutableList<NewsModel> = mutableListOf()
):
    RecyclerView.Adapter<SearchAdapter.ViewHolder>(),Filterable {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: AppCompatTextView = itemView.findViewById(R.id.tvNewsTitle)
        val imageView: AppCompatImageView = itemView.findViewById(R.id.imgNews)
        val textViewSource: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val textViewDescription: AppCompatTextView = itemView.findViewById(R.id.tvDescription)
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
        loadImage(itemList[position], holder.imageView)
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
    fun updateList(newList: List<NewsModel>) {
        fullList.clear()
        fullList.addAll(newList)
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullList
                } else {
                    val query = constraint.toString().lowercase(Locale.getDefault()).trim()
                    fullList.filter { news ->
                        (news.name?.lowercase(Locale.getDefault())?.contains(query) == true)
                    }.toMutableList()
                }
                return FilterResults().apply { values = filteredList }
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemList.clear()
                if (results?.values != null) {
                    itemList.addAll(results.values as List<NewsModel>)
                }
                notifyDataSetChanged()
            }

        }
    }
}