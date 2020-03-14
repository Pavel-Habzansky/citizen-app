package com.pavelhabzansky.citizenapp.features.settings.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemSettingsPlacesBinding
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceTypeVO

class PlaceSettingsAdapter(
        private val onCheck: (PlaceTypeVO, Boolean) -> Unit
) : RecyclerView.Adapter<PlaceSettingsAdapter.PlaceSettingsViewHolder>() {

    private val currentSettings = mutableSetOf<PlaceTypeVO>()

    fun setSettings(settings: Set<PlaceTypeVO>) {
        currentSettings.clear()
        currentSettings.addAll(settings)
        notifyDataSetChanged()
    }

    override fun getItemCount() = PlaceTypeVO.values().size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceSettingsViewHolder {
        val binding = DataBindingUtil.inflate<ItemSettingsPlacesBinding>(LayoutInflater.from(parent.context), R.layout.item_settings_places, parent, false)
        return PlaceSettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceSettingsViewHolder, position: Int) {
        holder.bind(PlaceTypeVO.values()[position])
    }

    inner class PlaceSettingsViewHolder(private val binding: ItemSettingsPlacesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(type: PlaceTypeVO) {
            binding.setVariable(BR.type, type)
            binding.executePendingBindings()

            binding.img.setImageResource(type.icon)

            binding.itemCheckbox.isChecked = currentSettings.contains(type)

            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked -> onCheck(type, isChecked) }
        }

    }

}