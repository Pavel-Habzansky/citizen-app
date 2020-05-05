package com.pavelhabzansky.citizenapp.features.news.view.adapter

import android.graphics.Bitmap
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.rjeschke.txtmark.Processor
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.hide
import com.pavelhabzansky.citizenapp.databinding.ItemEventBinding
import com.pavelhabzansky.citizenapp.features.news.view.vo.ScheduleViewObject
import io.noties.markwon.Markwon

class EventsAdapter(
        private val onClick: (ScheduleViewObject) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    var items: List<ScheduleViewObject> = emptyList()

    fun updateItems(newItems: List<ScheduleViewObject>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = DataBindingUtil.inflate<ItemEventBinding>(LayoutInflater.from(parent.context), R.layout.item_event, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScheduleViewObject) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()

            val markwon = Markwon.create(binding.root.context)
            markwon.setMarkdown(binding.eventText, item.text)

            if (item.pricing.isEmpty()) {
                binding.price.hide()
            }

            if (item.bitmap != null) {
                binding.eventImage.setImageBitmap(item.bitmap)
            }

            binding.root.setOnClickListener { onClick(item) }
        }

    }

}