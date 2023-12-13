package com.example.kpfu_itis_android2023.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kpfu_itis_android2023.data.db.entity.UserFilmFavoriteCrossRef
import com.example.kpfu_itis_android2023.data.db.entity.UserWithFavoriteFilms

@Dao
interface UserFilmFavoriteDao {
    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getUserWithFavoriteFilms(userId: Int): UserWithFavoriteFilms

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorites(userFilmFavoriteCrossRef: UserFilmFavoriteCrossRef)

    @Delete
    fun removeFromFavorites(userFilmFavoriteCrossRef: UserFilmFavoriteCrossRef)

    @Query("SELECT * FROM user_film_favorite WHERE userId = :userId AND filmId = :filmId")
    fun isFilmFavoriteForUser(userId: Int, filmId: Int): UserFilmFavoriteCrossRef?

    @Query("SELECT filmId FROM user_film_favorite WHERE userId = :userId")
    fun getFavoriteFilmIdsForUser(userId: Int): List<Int>
}
