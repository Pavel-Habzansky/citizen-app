package com.pavelhabzansky.citizenapp.features.filter.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemCityFilterBinding
import com.pavelhabzansky.citizenapp.features.filter.view.vo.CitySettingVO

class CityFilterAdapter(
        private val onCheck: (CitySettingVO) -> Unit
) : RecyclerView.Adapter<CityFilterAdapter.CityFilterViewHolder>() {

    private var items: List<CitySettingVO> = emptyList()

    fun updateSettings(newItems: List<CitySettingVO>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityFilterViewHolder {
        val binding = DataBindingUtil.inflate<ItemCityFilterBinding>(
                LayoutInflater.from(parent.context), R.layout.item_city_filter,
                parent, false
        )

        return CityFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityFilterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CityFilterViewHolder(private val binding: ItemCityFilterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CitySettingVO) {
            binding.itemCheckbox.text = item.name
            binding.itemCheckbox.isChecked = item.enabled

            binding.itemCheckbox.setOnClickListener {
                item.enabled = binding.itemCheckbox.isChecked
                onCheck(item)
            }
        }

    }
}