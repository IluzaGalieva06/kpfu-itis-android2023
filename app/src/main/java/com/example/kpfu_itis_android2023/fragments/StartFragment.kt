package com.example.optional.fragments

import android.os.Bundle
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
            btn?.setOnClickListener {
                val questionCount = etCount?.text.toString().toInt()
                val Fragment = MainFragment.newInstance(questionCount)
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