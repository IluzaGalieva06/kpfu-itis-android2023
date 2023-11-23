package com.example.kpfu_itis_android2023.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.kpfu_itis_android2023.Notifications
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.SettingsNotification
import com.example.kpfu_itis_android2023.databinding.FragmentSettingsBinding
import com.google.android.material.textfield.TextInputLayout


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _viewBinding: FragmentSettingsBinding? = null
    private val viewBinding: FragmentSettingsBinding
        get() = _viewBinding!!

    private val importanceOptions = arrayOf("Medium", "High", "Urgent")
    private val privacyOptions = arrayOf("Public", "Secret", "Private")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentSettingsBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    importanceOptions
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            importanceSpinner.adapter = adapter
//            val importanceInputLayout: TextInputLayout = viewBinding.importanceInputLayout

            importanceSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Notifications.importance = importanceOptions[position]
                        importanceInputLayout.editText?.setText(importanceOptions[position])
                        SettingsNotification.importance = importanceOptions[position]

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            val adapter1 =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, privacyOptions)
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            privacySpinner.adapter = adapter1
            privacySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Notifications.visibility = privacyOptions[position]
                        privacySpinner.setSelection(position)
                        SettingsNotification.visibility = privacyOptions[position]

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
//            val checkboxDetailedText = viewBinding.checkboxDetailedText
//            val checkboxDisplayButtons = viewBinding.checkboxDisplayButtons

            checkboxDetailedText.setOnCheckedChangeListener { _, isChecked ->
                Notifications.isBigText = isChecked
                SettingsNotification.isDetailedText = isChecked
            }


            checkboxDisplayButtons.setOnCheckedChangeListener { _, isChecked ->
                Notifications.showActions = isChecked
                SettingsNotification.isShowButtons = isChecked
            }
            loadSettingsState()
        }

    }


    private fun loadSettingsState() {
        val importance = SettingsNotification.importance
        val visibility = SettingsNotification.visibility
        val isBigText = SettingsNotification.isDetailedText
        val showActions = SettingsNotification.isShowButtons
        with(viewBinding) {

            importanceSpinner.setSelection(importanceOptions.indexOf(importance))
            privacySpinner.setSelection(privacyOptions.indexOf(visibility))
            checkboxDetailedText.isChecked = isBigText
            checkboxDisplayButtons.isChecked = showActions
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}