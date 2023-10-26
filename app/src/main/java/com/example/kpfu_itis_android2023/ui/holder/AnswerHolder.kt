package com.example.kpfu_itis_android2023.ui.holder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.data.Answer
import com.example.kpfu_itis_android2023.databinding.ItemAnswerBinding

class AnswerHolder(
    private val binding: ItemAnswerBinding,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.rbAnswer.setOnClickListener {
            onItemChecked.invoke(adapterPosition)
        }
        binding.root.setOnClickListener {
            onRootClicked.invoke(adapterPosition)
        }
    }

    fun onBind(answer: Answer) {
        with(binding) {
            tvAnswer.text = answer.answerText
            rbAnswer.isChecked = answer.isSelected
            rbAnswer.isEnabled = !answer.isSelected
            root.foreground = if (answer.isSelected) {
                null
            } else {
                ColorDrawable(Color.TRANSPARENT)
            }
        }
    }
}