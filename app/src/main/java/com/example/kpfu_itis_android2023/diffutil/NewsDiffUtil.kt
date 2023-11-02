package com.example.kpfu_itis_android2023.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.kpfu_itis_android2023.model.NewsDataModel

class NewsDiffUtil(
    private val oldList: List<NewsDataModel>,
    private val newList: List<NewsDataModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].newsId == newList[newItemPosition].newsId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        return newList[newItemPosition].isFavorite != oldList[oldItemPosition].isFavorite
    }

    companion object {
        fun calculateNewsDiff(
            oldList: List<NewsDataModel>,
            newList: List<NewsDataModel>
        ): DiffUtil.DiffResult {
            return DiffUtil.calculateDiff(NewsDiffUtil(oldList, newList))
        }
    }
}