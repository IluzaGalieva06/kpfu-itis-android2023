package com.example.kpfu_itis_android2023.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.base.BaseActivity
import com.example.kpfu_itis_android2023.base.BaseFragment
import com.example.kpfu_itis_android2023.databinding.FragmentFirstBinding
import com.example.kpfu_itis_android2023.utils.ActionType
import com.example.kpfu_itis_android2023.utils.ParamsKey

class FirstFragment : BaseFragment(R.layout.fragment_first) {
    private var _viewBinding: FragmentFirstBinding? = null
    private val viewBinding: FragmentFirstBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentFirstBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            btn.setOnClickListener {
                (requireActivity() as? BaseActivity)?.goToScreen(
                    actionType = ActionType.REPLACE,
                    destination = SecondFragment.newInstance(message = etFirst.text.toString()),
                    tag = SecondFragment.SECOND_FRAGMENT_TAG,
                    isAddToBackStack = true,
                )

                (requireActivity() as? BaseActivity)?.goToScreen(
                    actionType = ActionType.REPLACE,
                    destination = ThirdFragment.newInstance(message = etFirst.text.toString()),
                    tag = ThirdFragment.THIRD_FRAGMENT_TAG,
                    isAddToBackStack = true,
                )
            }
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT_TAG"

        fun newInstance(message: String) = FirstFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}