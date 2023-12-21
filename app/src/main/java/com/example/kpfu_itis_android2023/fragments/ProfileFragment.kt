package com.example.kpfu_itis_android2023.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.data.db.AppDatabase
import com.example.kpfu_itis_android2023.data.db.dao.UsersDao
import com.example.kpfu_itis_android2023.databinding.FragmentProfileBinding
import com.example.kpfu_itis_android2023.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _viewBinding: FragmentProfileBinding? = null
    private val viewBinding: FragmentProfileBinding
        get() = _viewBinding!!
    private lateinit var userDao: UsersDao


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentProfileBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentProfileBinding.bind(view)

        val sharedPreferences =
            requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getInt("user_id", -1)


        val db = AppDatabase.getInstance(requireContext())
        userDao = db.userDao()
        with(viewBinding) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val currentUser = userDao.getUserInfoById(userId)
                    currentUser?.let {
                        textViewName.text = "Имя: ${it.name}"
                        textViewEmail.text = "Email: ${it.emailAddress}"
                        textViewPhone.text = "Номер телефона: ${it.phoneNumber}"

                        buttonInEditTextPhone.setOnClickListener { phone ->
                            val newPhoneNumber = editTextPhone.text.toString()

                            if (newPhoneNumber != it.phoneNumber) {
                                lifecycleScope.launch {
                                    withContext(Dispatchers.IO) {
                                        val currentUserPhone =
                                            userDao.getUserByPhone(newPhoneNumber)
                                        if (currentUserPhone == null) {
                                            userDao.updatePhoneNumber(userId, newPhoneNumber)
                                            textViewPhone.text = "Номер телефона: $newPhoneNumber"
                                            editTextPhone.setText("")
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    R.string.phone_number_exists,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                    }
                                }
                            } else {

                                Toast.makeText(
                                    requireContext(),
                                    R.string.phone_number_not_changed,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }
                }

                buttonInEditTextPassword.setOnClickListener {
                    val newPassword = editTextPassword.text.toString()
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            userDao.updatePassword(userId, newPassword)
                            withContext(Dispatchers.Main) {
                                editTextPassword.setText("")

                            }
                        }
                    }
                }
                buttonDeleteAccount.setOnClickListener {
                    lifecycleScope.launch {
                        val currentTimeMillis = System.currentTimeMillis()
                        withContext(Dispatchers.IO) {
                            userDao.updateDeletedAt(userId, currentTimeMillis)

                            withContext(Dispatchers.Main) {
                                sharedPreferences.edit().clear().apply()
                                val sessionManager = SessionManager(requireContext())
                                sessionManager.clearSession()
                                activity?.supportFragmentManager?.beginTransaction()?.apply {
                                    replace(
                                        R.id.main_activity_container,
                                        AuthorizationFragment()
                                    )
                                    addToBackStack(null)
                                    commit()
                                }
                            }
                        }
                    }
                }

                buttonLogout.setOnClickListener {
                    val sessionManager = SessionManager(requireContext())
                    sessionManager.clearSession()
                    activity?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(
                            R.id.main_activity_container,
                            AuthorizationFragment()
                        )
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}