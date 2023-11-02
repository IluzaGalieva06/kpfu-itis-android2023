package com.example.kpfu_itis_android2023.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.adapter.NewsAdapter
import com.example.kpfu_itis_android2023.diffutil.NewsDiffUtil
import com.example.kpfu_itis_android2023.model.NewsDataModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.kpfu_itis_android2023.util.NewsDataRepository


class NewsBottomSheet(private val newsAdapter: NewsAdapter) : BottomSheetDialogFragment() {

    private lateinit var addButton: Button
    private lateinit var numberEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)

        addButton = view.findViewById(R.id.addButton)
        numberEditText = view.findViewById(R.id.numberEditText)

        addButton.setOnClickListener {
            val number = numberEditText.text.toString().toIntOrNull()
            if (number != null && number in 1..5) {
                addNewsItems(number)
                dismiss()
            } else {
                Toast.makeText(context, "Введите число от 1 до 5", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun addNewsItems(count: Int) {
        val currentList = newsAdapter?.getItems() ?: emptyList()
        val newList = generateRandomNews(count)

        newsAdapter?.let {

            val diffResult = NewsDiffUtil.calculateNewsDiff(currentList, newList)
            it.setItems(newList)
            diffResult.dispatchUpdatesTo(it)
        }
    }

    private fun generateRandomNews(count: Int): List<NewsDataModel> {
        val availableNews = NewsDataRepository().allQuestions
        val currentNews = newsAdapter?.getItems() ?: emptyList()

        val newNews = mutableListOf<NewsDataModel>()
        newNews.addAll(currentNews)

        repeat(count) {
            val randomIndex = (0 until newNews.size + 1).random()
            val randomNews = availableNews.random()

            newNews.add(randomIndex, randomNews)
        }

        return newNews
    }
}