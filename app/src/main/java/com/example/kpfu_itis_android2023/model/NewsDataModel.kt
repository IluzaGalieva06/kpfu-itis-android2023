package com.example.kpfu_itis_android2023.model

import androidx.annotation.DrawableRes

data class NewsDataModel(
    val newsId: Int,
    val newsTitle: String,
    val newsDetails: String? = null,
    @DrawableRes val newsImage: Int? = null,
    var isFavorite: Boolean = false
)