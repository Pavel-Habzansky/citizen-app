package com.pavelhabzansky.citizenapp.features.events.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemPushListBinding
import com.pavelhabzansky.citizenapp.features.events.view.vo.PushEventVO

class PushEventListAdapter(
        private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<PushEventListAdapter.PushEventViewHolder>() {

    private var items: List<PushEventVO> = emptyList()

    fun updateItems(newItems: List<PushEventVO>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PushEventViewHolder {
        val binding = DataBindingUtil.inflate<ItemPushListBinding>(LayoutInflater.from(parent.context), R.layout.item_push_list, parent, false)
        return PushEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PushEventViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class PushEventViewHolder(private val binding: ItemPushListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PushEventVO) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()

            binding.removeIc.setOnClickListener { onRemoveClick(item.id) }
        }

    }
}