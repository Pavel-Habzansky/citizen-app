package com.pavelhabzansky.citizenapp.features.news.view.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fromRssDate
import com.pavelhabzansky.citizenapp.databinding.ItemNewsItemBinding
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject
import kotlinx.android.synthetic.main.item_news_item.view.*

class NewsSourceAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    private var items: List<NewsItemViewObject> = emptyList()

    fun setItems(newItems: List<NewsItemViewObject>) {
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
        val binding = DataBindingUtil.inflate<ItemNewsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_news_item,
            parent,
            false
        )

        return NewsSourceViewHolder(binding = binding, onItemClick = onItemClick).also { it.setIsRecyclable(false) }
    }

    inner class NewsSourceViewHolder(
        private val binding: ItemNewsItemBinding,
        private val onItemClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: NewsItemViewObject) {
            binding.setVariable(BR.item, model)
            binding.executePendingBindings()

            binding.root.setOnClickListener { onItemClick(model.title) }
            if (model.read) {
                binding.root.messageTitle.setTypeface(null, Typeface.NORMAL)
            }
        }

    }

}
