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
import com.example.kpfu_itis_android2023.databinding.FragmentSecondBinding
import com.example.kpfu_itis_android2023.utils.ActionType
import com.example.kpfu_itis_android2023.utils.ParamsKey

class SecondFragment : BaseFragment(R.layout.fragment_second) {
    private var _viewBinding: FragmentSecondBinding? = null
    private val viewBinding: FragmentSecondBinding
        get() = _viewBinding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentSecondBinding.bind(view)
        with(viewBinding) {

            arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)?.let { message ->
                tvSecond.text =
                    if (message.isNotEmpty()) message else getString(R.string.second_fragment)


                btnThird.setOnClickListener {
                    parentFragmentManager.popBackStack()

                    (requireActivity() as? BaseActivity)?.goToScreen(
                        actionType = ActionType.REPLACE,
                        destination = ThirdFragment.newInstance(message = message),
                        tag = ThirdFragment.THIRD_FRAGMENT_TAG,
                        isAddToBackStack = true
                    )
                }

                btnFirst.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT_TAG"

        fun newInstance(message: String) = SecondFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}