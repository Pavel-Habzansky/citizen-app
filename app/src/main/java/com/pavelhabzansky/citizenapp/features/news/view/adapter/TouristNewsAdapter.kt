package com.pavelhabzansky.citizenapp.features.news.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemNewsItemBinding
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject

class TouristNewsAdapter : RecyclerView.Adapter<TouristNewsAdapter.TouristNewsViewHolder>() {

    private var items: List<NewsItemViewObject> = emptyList()

    fun setItems(newItems: List<NewsItemViewObject>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TouristNewsViewHolder {
        val binding = DataBindingUtil.inflate<ItemNewsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_news_item,
            parent,
            false
        )

        return TouristNewsViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TouristNewsViewHolder, position: Int) {
        holder.bind(model = items[position])
    }

    inner class TouristNewsViewHolder(private val binding: ItemNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: NewsItemViewObject) {

        }


    }

}