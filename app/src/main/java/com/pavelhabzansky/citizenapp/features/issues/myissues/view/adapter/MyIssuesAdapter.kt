package com.pavelhabzansky.citizenapp.features.issues.myissues.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.timestampToString
import com.pavelhabzansky.citizenapp.databinding.ItemMyIssuesBinding
import com.pavelhabzansky.citizenapp.features.issues.myissues.view.vo.MyIssueVO

class MyIssuesAdapter : RecyclerView.Adapter<MyIssuesAdapter.MyIssueViewHolder>() {

    private var items: List<MyIssueVO> = emptyList()

    fun updateItems(newItems: List<MyIssueVO>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyIssueViewHolder {
        val binding = DataBindingUtil.inflate<ItemMyIssuesBinding>(LayoutInflater.from(parent.context), R.layout.item_my_issues, parent, false)
        return MyIssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyIssueViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MyIssueViewHolder(private val binding: ItemMyIssuesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyIssueVO) {
            binding.title.text = item.title
            binding.date.text = item.date.timestampToString()
            binding.description.text = item.description
            binding.issueImage.setImageBitmap(item.image)
        }

    }
}