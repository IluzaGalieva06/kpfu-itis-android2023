package com.example.kpfu_itis_android2023.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "film_ratings",
    primaryKeys = ["userId", "filmId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FilmEntity::class,
            parentColumns = ["filmId"],
            childColumns = ["filmId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FilmRatingEntity(
    val userId: Int,
    val filmId: Int,
    val rating: Float
)