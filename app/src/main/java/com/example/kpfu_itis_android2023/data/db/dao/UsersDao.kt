package com.example.kpfu_itis_android2023.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kpfu_itis_android2023.data.db.entity.UserEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userData: UserEntity)

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<UserEntity>?

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getUserInfoById(userId: Int): UserEntity?

    @Query("DELETE FROM user WHERE userId = :userId")
    fun deleteUser(userId: Int)

    @Query("UPDATE user SET phone_number = :phoneNumber WHERE userId = :userId")
    fun updatePhoneNumber(userId: Int, phoneNumber: String)

    @Query("UPDATE user SET password = :newPassword WHERE userId = :userId")
    fun updatePassword(userId: Int, newPassword: String)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    @Query("SELECT * FROM user WHERE phone_number = :phoneNumber")
    fun getUserByPhone(phoneNumber: String): UserEntity?

    @Query("SELECT userId FROM user WHERE email = :email AND password = :password")
    fun getUserIdByEmailAndPassword(email: String, password: String): Int?

    @Query("UPDATE user SET deleted_at = :deletedAt WHERE userId = :userId")
    fun updateDeletedAt(userId: Int, deletedAt: Long)

    @Query("SELECT * FROM user WHERE deleted_at != 0 AND deleted_at < :timeThreshold")
    fun getUsersForDeletion(timeThreshold: Long): List<UserEntity>?


}
