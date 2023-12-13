package com.example.kpfu_itis_android2023.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFavoriteFilms(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "filmId",
        associateBy = Junction(UserFilmFavoriteCrossRef::class)
    )
    val favoriteFilms: List<FilmEntity>
)
