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
    private var phoneValid = false
    private var questionCountValid = false

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
        initTextChangedListeners()

    }

    private fun initTextChangedListeners() {
        with(viewBinding) {

            tvPhoneNumber.apply {
                addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(
                        input: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        input: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        input?.let {
                            if (start == 0) {
                                tvPhoneNumber.removeTextChangedListener(this)
                                tvPhoneNumber.setText("+7(9")
                                tvPhoneNumber.setSelection(tvPhoneNumber.text?.length ?: 0)
                                tvPhoneNumber.addTextChangedListener(this)
                            }
                            if (input.length == 6) {
                                tvPhoneNumber.removeTextChangedListener(this)
                                tvPhoneNumber.setText("$input)-")
                                tvPhoneNumber.setSelection(tvPhoneNumber.text?.length ?: 0)
                                tvPhoneNumber.addTextChangedListener(this)

                            }
                            if (input.length == 8) {
                                tvPhoneNumber.removeTextChangedListener(this)
                                tvPhoneNumber.setText("$input-")
                                tvPhoneNumber.setSelection(tvPhoneNumber.text?.length ?: 0)
                                tvPhoneNumber.addTextChangedListener(this)

                            }
                            if (input.length == 11) {
                                tvPhoneNumber.removeTextChangedListener(this)
                                tvPhoneNumber.setText("$input-")
                                tvPhoneNumber.setSelection(tvPhoneNumber.text?.length ?: 0)
                                tvPhoneNumber.addTextChangedListener(this)

                            }
                            if (input.length == 14) {
                                tvPhoneNumber.removeTextChangedListener(this)
                                tvPhoneNumber.setText("$input-")
                                tvPhoneNumber.setSelection(tvPhoneNumber.text?.length ?: 0)
                                tvPhoneNumber.addTextChangedListener(this)

                            }
                            if (input.length > 17) {
                                tvPhoneNumber.error = context?.getString(R.string.number_error)

                            }

                            phoneValid = input.length == 17


                        }
                    }

                    override fun afterTextChanged(input: Editable?) {

                    }
                })

            }


            tvCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val questionCount = s?.toString()?.toIntOrNull() ?: 0
                    questionCountValid = questionCount in 1..12
                    if (!questionCountValid) {
                        tvCount.error = context?.getString(R.string.count_error)
                    }
                    updateStartButtonAvailability()

                }

                override fun afterTextChanged(s: Editable?) {}
            })
            btnStart?.setOnClickListener {
                val questionCount = tvCount?.text.toString().toInt()
                val viewPagerFragment = ViewPagerFragment.newInstance(questionCount)
                parentFragmentManager
                    .beginTransaction()
                    .addToBackStack("StartFragment")
                    .replace(R.id.main_fragment_container, viewPagerFragment)
                    .commit()

            }
        }
    }

    private fun updateStartButtonAvailability() {
        viewBinding?.btnStart?.isEnabled = phoneValid && questionCountValid
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val START_FRAGMENT_TAG = "START_FRAGMENT_TAG"
        fun getInstance(): StartFragment {
            return StartFragment()
        }
    }
}