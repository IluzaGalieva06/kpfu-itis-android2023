package com.example.kpfu_itis_android2023.base

import androidx.appcompat.app.AppCompatActivity
import com.example.kpfu_itis_android2023.utils.ActionType

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val fragmentContainerId: Int

    abstract fun goToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        tag: String? = null,
        isAddToBackStack: Boolean = true,
    )
}