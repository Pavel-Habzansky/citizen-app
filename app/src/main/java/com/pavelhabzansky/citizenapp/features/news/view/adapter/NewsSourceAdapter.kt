package com.pavelhabzansky.citizenapp.features.news.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemNewsSourceBinding
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsSourceViewObject

class NewsSourceAdapter : RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    private var items: List<NewsSourceViewObject> = emptyList()

    fun setItems(newItems: List<NewsSourceViewObject>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        holder.bind(model = items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val binding = DataBindingUtil.inflate<ItemNewsSourceBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_news_source,
            parent,
            false
        )

        return NewsSourceViewHolder(binding = binding)
    }

    inner class NewsSourceViewHolder(private val binding: ItemNewsSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: NewsSourceViewObject) {

        }

    }

}
