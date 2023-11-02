package com.example.kpfu_itis_android2023.ui.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R

class DateViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

    fun onBindDateItem() {
        dateTextView.text = "01.11.2023"
    }
}