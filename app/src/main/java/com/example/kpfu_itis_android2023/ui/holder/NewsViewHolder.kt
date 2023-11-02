package com.example.kpfu_itis_android2023.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.ItemNewsBinding
import com.example.kpfu_itis_android2023.model.NewsDataModel

class NewsViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private var item: NewsDataModel? = null
    val viewBinding: ItemNewsBinding = ItemNewsBinding.bind(item)


    fun onBindNewsItem(item: NewsDataModel) = with(viewBinding) {
        this@NewsViewHolder.item = item
        tvTitle.text = item.newsTitle
        ivCover.transitionName = "transitionNameForItem_${adapterPosition}"
        item.newsImage?.let { res ->
            ivCover.setImageResource(res)
        }
        changeLikeBtnStatus(isChecked = item.isFavorite)

    }


    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable = if (isChecked) R.drawable.ic_like_red else R.drawable.ic_like_gray
        viewBinding.btnFavorite.setImageResource(likeDrawable)
    }
}