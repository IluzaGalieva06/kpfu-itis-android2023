package com.example.kpfu_itis_android2023.ui.holder

import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.adapter.NewsAdapter
import com.example.kpfu_itis_android2023.ui.fragments.NewsBottomSheet

class ButtonViewHolder(
    item: View,
    private val newsAdapter: NewsAdapter,
    private val fragmentManager: FragmentManager
) : RecyclerView.ViewHolder(item) {
    init {
        val button = itemView.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = NewsBottomSheet(newsAdapter)
        bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)

    }
}