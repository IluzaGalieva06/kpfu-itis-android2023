package com.example.kpfu_itis_android2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kpfu_itis_android2023.base.BaseActivity
import com.example.kpfu_itis_android2023.base.BaseFragment
import com.example.kpfu_itis_android2023.fragments.FirstFragment
import com.example.kpfu_itis_android2023.utils.ActionType

class MainActivity : BaseActivity() {
    override val fragmentContainerId: Int = R.id.main_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    fragmentContainerId,
                    FirstFragment(),
                    FirstFragment.FIRST_FRAGMENT_TAG
                )
                .commit()
        }
    }

    override fun goToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        tag: String?,
        isAddToBackStack: Boolean
    ) {
        supportFragmentManager.beginTransaction().apply {
            when (actionType) {
                ActionType.ADD -> {
                    this.add(fragmentContainerId, destination, tag)
                }

                ActionType.REPLACE -> {
                    this.replace(fragmentContainerId, destination, tag)
                }

                ActionType.REMOVE -> {
                    this.remove(destination)
                }

                else -> Unit
            }
            if (isAddToBackStack) {
                this.addToBackStack(null)
            }
        }.commit()
    }
}