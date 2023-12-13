package com.example.kpfu_itis_android2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kpfu_itis_android2023.data.db.AppDatabase
import com.example.kpfu_itis_android2023.databinding.ActivityMainBinding
import com.example.kpfu_itis_android2023.fragments.AddMovieFragment
import com.example.kpfu_itis_android2023.fragments.AuthorizationFragment
import com.example.kpfu_itis_android2023.fragments.MainFragment
import com.example.kpfu_itis_android2023.fragments.ProfileFragment
import com.example.kpfu_itis_android2023.util.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        sessionManager = SessionManager(this)
        val currentDate = System.currentTimeMillis()

        if (sessionManager.isUserLoggedIn()) {
            removeUsersIfTwoMinutesPassed(currentDate)
            replaceFragment(MainFragment())
        } else {
            removeUsersIfTwoMinutesPassed(currentDate)
            replaceFragment(AuthorizationFragment())
        }

        findViewById<BottomNavigationView>(R.id.main_bottom_navigation)?.let {
            it.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.main_page -> {
                        replaceFragment(MainFragment())
                        true
                    }

                    R.id.main_add -> {
                        replaceFragment(AddMovieFragment())
                        true
                    }

                    R.id.main_profile -> {
                        replaceFragment(ProfileFragment())
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun removeUsersIfTwoMinutesPassed(currentTimeMillis: Long) {
        val userDao = AppDatabase.getInstance(this).userDao()

        val sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val usersToDelete =
                    userDao.getUsersForDeletion(currentTimeMillis - sevenDaysInMillis)

                usersToDelete?.forEach { user ->
                    userDao.deleteUser(user.userId)
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_activity_container, fragment, fragment.javaClass.name)
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}