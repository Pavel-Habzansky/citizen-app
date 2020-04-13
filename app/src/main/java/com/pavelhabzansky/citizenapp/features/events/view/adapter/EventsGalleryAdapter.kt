package com.pavelhabzansky.citizenapp.features.events.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemEventGalleryBinding

class EventsGalleryAdapter : RecyclerView.Adapter<EventsGalleryAdapter.EventGalleryViewHolder>() {

    private var items: List<Bitmap> = emptyList()

    fun updateGallery(gallery: List<Bitmap>) {
        items = gallery
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventGalleryViewHolder {
        val binding = DataBindingUtil.inflate<ItemEventGalleryBinding>(LayoutInflater.from(parent.context), R.layout.item_event_gallery, parent, false)
        return EventGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventGalleryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class EventGalleryViewHolder(private val binding: ItemEventGalleryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(img: Bitmap) {
            binding.imageHolder.setImageBitmap(img)
        }

    }

}