package com.pavelhabzansky.citizenapp.features.issues.list.view.adapter

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

class IssuesDiffCallback(
        private val oldList: List<IssueVO>,
        private val newList: List<IssueVO>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (titleOld, _, typeOld, descriptionOld) = oldList[oldItemPosition]
        val (titleNew, _, typeNew, descriptionNew) = newList[newItemPosition]
        return titleOld == titleNew && typeOld == typeNew && descriptionOld == descriptionNew
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}