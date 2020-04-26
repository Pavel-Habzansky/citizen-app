package com.pavelhabzansky.citizenapp.features.issues.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemMapItemBinding
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.MapItem
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO

class ItemListAdapter(
        private val onClick: (MapItem) -> Unit
) : RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    private var items: MutableList<MapItem> = mutableListOf()

    fun updateItems(newItems: List<MapItem>) {
        items = newItems.toMutableList()

        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding = DataBindingUtil.inflate<ItemMapItemBinding>(
                LayoutInflater.from(parent.context), R.layout.item_map_item, parent, false
        )
        return ItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ItemListViewHolder(private val binding: ItemMapItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MapItem) {
            binding.root.setOnClickListener { onClick(item) }

            when (item) {
                is IssueVO -> {
                    binding.itemTitle.text = item.issueTitle
                    binding.itemDescription.text = item.description
                    binding.itemImage.setImageResource(item.type.icon)
                }
                is PlaceVO -> {
                    binding.itemTitle.text = item.name
                    binding.itemDescription.text = item.vicinity
                    binding.itemImage.setImageResource(item.type.icon)
                }
            }
        }

    }
}