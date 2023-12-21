package com.example.kpfu_itis_android2023.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kpfu_itis_android2023.data.db.entity.FilmEntity

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFilm(film: FilmEntity)

    @Query("SELECT * FROM film WHERE title = :title AND year = :year")
    fun getFilmByTitleAndYear(title: String, year: Int): FilmEntity?

    @Query("SELECT * FROM film")
    fun getAllFilms(): List<FilmEntity>?

    @Delete
    fun deleteFilm(film: FilmEntity)

    @Update
    fun updateFilm(film: FilmEntity)

}