package com.example.kpfu_itis_android2023.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.base.BaseFragment
import com.example.kpfu_itis_android2023.databinding.FragmentThirdBinding
import com.example.kpfu_itis_android2023.utils.ParamsKey

class ThirdFragment : BaseFragment(R.layout.fragment_third) {
    private var _viewBinding: FragmentThirdBinding? = null
    private val viewBinding: FragmentThirdBinding
        get() = _viewBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentThirdBinding.bind(view)

        arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)?.let { message ->
            viewBinding.tvThird.text =
                if (message.isNotEmpty()) message else getString(R.string.third_fragment)

        }
    }

    companion object {
        const val THIRD_FRAGMENT_TAG = "THIRD_FRAGMENT_TAG"

        fun newInstance(message: String) = ThirdFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }

}