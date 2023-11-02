package com.example.kpfu_itis_android2023.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private var _viewBinding: FragmentStartBinding? = null
    private val viewBinding: FragmentStartBinding
        get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentStartBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(viewBinding) {
            etCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val questionCount = etCount.text.toString().toIntOrNull() ?: 0
                    if (questionCount in 0..45) {
                        etCount.error = null
                    } else {
                        etCount.error = context?.getString(R.string.count_error)
                    }

                }

                override fun afterTextChanged(s: Editable?) {}
            })
            btn?.setOnClickListener {
                val questionCount = etCount?.text.toString().toInt()
                val Fragment = NewsFragment.newInstance(questionCount)
                parentFragmentManager
                    .beginTransaction()
                    .addToBackStack("StartFragment")
                    .replace(R.id.main_activity_container, Fragment)
                    .commit()

            }

        }
    }

    companion object {
        const val START_FRAGMENT_TAG = "START_FRAGMENT_TAG"
        fun getInstance(): StartFragment {
            return StartFragment()
        }
    }
}