package com.example.kpfu_itis_android2023.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kpfu_itis_android2023.data.db.entity.FilmRatingEntity

@Dao
interface FilmRatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun rateFilm(filmRating: FilmRatingEntity)

    @Query("SELECT rating FROM film_ratings WHERE userId = :userId AND filmId = :filmId")
    fun getRatingForFilm(userId: Int, filmId: Int): Float?
}