package com.pavelhabzansky.citizenapp.features.cities.search.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemLastSearchBinding
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.LastCitySearchVO

class LastSearchAdapter(
    private val onClick: (item: LastCitySearchVO) -> Unit,
    private var items: List<LastCitySearchVO> = emptyList()
) : RecyclerView.Adapter<LastSearchAdapter.LastSearchViewHolder>() {

    fun updateItems(newItems: List<LastCitySearchVO>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: LastSearchViewHolder, position: Int) {
        holder.bind(item = items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastSearchViewHolder {
        val binding = DataBindingUtil.inflate<ItemLastSearchBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_last_search,
            parent,
            false
        )

        return LastSearchViewHolder(binding = binding)
    }


    inner class LastSearchViewHolder(private val binding: ItemLastSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LastCitySearchVO) {
            binding.lastSearchText.text = item.name

            binding.root.setOnClickListener {
                onClick(item)
            }
        }

    }
}