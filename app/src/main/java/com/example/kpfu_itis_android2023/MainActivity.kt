package com.example.kpfu_itis_android2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kpfu_itis_android2023.databinding.ActivityMainBinding
import com.example.optional.fragments.StartFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.let {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.main_activity_container,
                    StartFragment.getInstance(),
                    StartFragment.START_FRAGMENT_TAG
                )
                .commit()
        }
    }
}