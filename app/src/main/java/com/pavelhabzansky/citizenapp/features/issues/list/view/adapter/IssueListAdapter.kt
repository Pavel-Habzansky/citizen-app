package com.pavelhabzansky.citizenapp.features.issues.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.ItemIssueBinding
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

class IssueListAdapter(
        private val onClick: (IssueVO) -> Unit
) : RecyclerView.Adapter<IssueListAdapter.IssueListViewHolder>() {

    private var items: MutableList<IssueVO> = mutableListOf()

    fun updateItems(newItems: List<IssueVO>) {
        items = newItems.toMutableList()

        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueListViewHolder {
        val binding = DataBindingUtil.inflate<ItemIssueBinding>(
                LayoutInflater.from(parent.context), R.layout.item_issue, parent, false
        )
        return IssueListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class IssueListViewHolder(private val binding: ItemIssueBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(issue: IssueVO) {
            binding.setVariable(BR.item, issue)
            binding.executePendingBindings()

            binding.root.setOnClickListener { onClick(issue) }

            binding.itemImage.setImageResource(issue.type.icon)
        }

    }
}