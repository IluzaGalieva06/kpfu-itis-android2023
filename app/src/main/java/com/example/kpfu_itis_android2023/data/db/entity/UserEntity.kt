package com.example.kpfu_itis_android2023.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val name: String,
    @ColumnInfo(name = "email")
    val emailAddress: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long = 0
)
