package com.pavelhabzansky.citizenapp.features.cities.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemCityGalleryBinding
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityGalleryItemVO

class CityGalleryAdapter(
        var items: List<CityGalleryItemVO> = emptyList()
) : RecyclerView.Adapter<CityGalleryAdapter.CityGalleryViewHolder>() {

    fun updateItems(newItems: List<CityGalleryItemVO>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityGalleryViewHolder {
        val binding = DataBindingUtil.inflate<ItemCityGalleryBinding>(
                LayoutInflater.from(parent.context), R.layout.item_city_gallery,
                parent, false
        )

        return CityGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityGalleryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CityGalleryViewHolder(private val binding: ItemCityGalleryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityGalleryItemVO) {
            binding.photo.setImageBitmap(item.image)

            binding.photo.setOnClickListener {
                
            }
        }

    }

}