package com.pavelhabzansky.citizenapp.features.settings.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemSettingsIssuesBinding
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO

class IssueSettingsAdapter(
        private val onCheck: (IssueTypeVO, Boolean) -> Unit
) : RecyclerView.Adapter<IssueSettingsAdapter.IssueSettingViewHolder>() {

    private val currentSettings = mutableSetOf<IssueTypeVO>()

    fun setSettings(settings: Set<IssueTypeVO>) {
        currentSettings.clear()
        currentSettings.addAll(settings)
        notifyDataSetChanged()
    }

    override fun getItemCount() = IssueTypeVO.values().size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueSettingViewHolder {
        val binding = DataBindingUtil.inflate<ItemSettingsIssuesBinding>(
                LayoutInflater.from(parent.context), R.layout.item_settings_issues,
                parent, false
        )

        return IssueSettingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueSettingViewHolder, position: Int) {
        holder.bind(IssueTypeVO.values()[position])
    }

    inner class IssueSettingViewHolder(private val binding: ItemSettingsIssuesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(type: IssueTypeVO) {
            binding.setVariable(BR.type, type)
            binding.executePendingBindings()

            binding.img.setImageResource(type.icon)

            binding.itemCheckbox.isChecked = currentSettings.contains(type)

            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked -> onCheck(type, isChecked) }
        }
    }
}