package com.pavelhabzansky.citizenapp.features.place.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemPlaceGalleryBinding

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private var images: List<Bitmap> = emptyList()

    fun setItems(items: List<Bitmap>) {
        images = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = images.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = DataBindingUtil.inflate<ItemPlaceGalleryBinding>(
                LayoutInflater.from(parent.context), R.layout.item_place_gallery,
                parent, false
        )

        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(images[position])
    }


    inner class GalleryViewHolder(private val binding: ItemPlaceGalleryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Bitmap) {
            binding.image.setImageBitmap(image)
        }

    }
}