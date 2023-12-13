package com.example.kpfu_itis_android2023.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.data.db.AppDatabase
import com.example.kpfu_itis_android2023.data.db.entity.UserEntity
import com.example.kpfu_itis_android2023.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var _viewBinding: FragmentRegistrationBinding? = null
    private val viewBinding: FragmentRegistrationBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentRegistrationBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(viewBinding) {
            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = editTextEmailRegister.text.toString()
                val password = etPasswordRegister.text.toString()
                val phone = etPhone.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty()) {
                    val userDao = AppDatabase.getInstance(requireContext()).userDao()
                    lifecycleScope.launch(Dispatchers.IO) {
                        val existingUser = userDao.getUserByEmailAndPassword(email, password)
                        val existingUserByPhone = userDao.getUserByPhone(phone)

                        if (existingUser == null) {
                            if(existingUserByPhone == null) {
                                val newUser = UserEntity(
                                    name = name,
                                    emailAddress = email,
                                    password = password,
                                    phoneNumber = phone
                                )

                                userDao.addUser(newUser)

                                activity?.supportFragmentManager?.popBackStack()
                            }else{
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.not_unique),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.there_is_user),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}