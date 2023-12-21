package com.example.kpfu_itis_android2023.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kpfu_itis_android2023.data.db.dao.FilmDao
import com.example.kpfu_itis_android2023.data.db.dao.FilmRatingDao
import com.example.kpfu_itis_android2023.data.db.dao.UserFilmFavoriteDao
import com.example.kpfu_itis_android2023.data.db.dao.UsersDao
import com.example.kpfu_itis_android2023.data.db.entity.FilmEntity
import com.example.kpfu_itis_android2023.data.db.entity.FilmRatingEntity
import com.example.kpfu_itis_android2023.data.db.entity.UserEntity
import com.example.kpfu_itis_android2023.data.db.entity.UserFilmFavoriteCrossRef

@Database(
    entities = [UserEntity::class, FilmEntity::class, FilmRatingEntity::class, UserFilmFavoriteCrossRef::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UsersDao
    abstract fun filmDao(): FilmDao
    abstract fun userFilmDao(): UserFilmFavoriteDao
    abstract fun filmRatingDao(): FilmRatingDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}