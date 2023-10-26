package com.example.itis_android23

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.ActivityMainBinding
import com.example.kpfu_itis_android2023.ui.fragments.StartFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.let {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.main_fragment_container,
                    StartFragment.getInstance(),
                    StartFragment.START_FRAGMENT_TAG
                )
                .commit()
        }
    }
}
