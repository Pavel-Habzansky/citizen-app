package com.pavelhabzansky.citizenapp.features.issues.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelhabzansky.citizenapp.databinding.ItemIssueBinding
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

class IssueListAdapter : RecyclerView.Adapter<IssueListAdapter.IssueListViewHolder>() {

    private var items: List<IssueVO> = emptyList()

    fun updateItems(newItems: List<IssueVO>) {
        // TODO Update items, use DiffUtil
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueListViewHolder {
        val binding = ItemIssueBinding.inflate(LayoutInflater.from(parent.context))
        return IssueListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class IssueListViewHolder(private val binding: ItemIssueBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(issue: IssueVO) {

        }

    }
}