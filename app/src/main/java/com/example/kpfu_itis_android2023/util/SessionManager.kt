package com.example.kpfu_itis_android2023.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "user_session",
        Context.MODE_PRIVATE
    )

    fun saveSession() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun clearSession() {
        sharedPreferences.edit().remove("isLoggedIn").apply()
    }
}
