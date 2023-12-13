package com.example.kpfu_itis_android2023.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "film")
data class FilmEntity(
    @PrimaryKey(autoGenerate = true) val filmId: Int = 0,
    val title: String,
    val year: Int,
    val description: String,
    val photoUrl: String,
)