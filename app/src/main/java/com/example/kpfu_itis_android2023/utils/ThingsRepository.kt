package com.example.optional.utils

import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.ThingsDataModel


object ThingsRepository {
    private val newsList = mutableListOf<ThingsDataModel>()

    init {
        initializeNewsList()
    }

    private fun initializeNewsList() {
        for (i in 0..77) {
            val imageId = when (i % 6) {
                0 -> R.drawable.item_1
                1 -> R.drawable.item_2
                2 -> R.drawable.item_3
                3-> R.drawable.item_4
                4 -> R.drawable.item_5
                else -> R.drawable.item_6
            }

            val things = ThingsDataModel(imageId)
            newsList.add(things)
        }
    }

    fun getNewsList(countQuestions: Int): List<ThingsDataModel> {
        newsList.clear()
        initializeNewsList()

        return newsList.take(countQuestions)
    }
}