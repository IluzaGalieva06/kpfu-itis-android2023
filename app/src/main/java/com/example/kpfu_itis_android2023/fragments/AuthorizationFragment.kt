package com.example.kpfu_itis_android2023.fragments

import android.app.AlertDialog
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
import com.example.kpfu_itis_android2023.databinding.FragmentAuthorizationBinding
import com.example.kpfu_itis_android2023.util.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private var _viewBinding: FragmentAuthorizationBinding? = null
    private val viewBinding: FragmentAuthorizationBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentAuthorizationBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)

        bottomNavigationView?.visibility = View.GONE
        login()
    }

    private fun login() {
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        with(viewBinding) {


            btnLogin.setOnClickListener {
                val email = etEmailAuthorization.text.toString()
                val password = etPasswordAuthorization.text.toString()


                lifecycleScope.launch {
                    val user = withContext(Dispatchers.IO) {
                        userDao.getUserByEmailAndPassword(email, password)
                    }
                    withContext(Dispatchers.Main) {

                        if (user != null) {
                            if (user.deletedAt > 0) {
                                val deletedTime = user.deletedAt
                                val currentTime = System.currentTimeMillis()
                                val elapsedDays =
                                    (currentTime - deletedTime) / (24 * 60 * 60 * 1000)
                                if (elapsedDays >= 7) {
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.IO) {
                                            userDao.deleteUser(user.userId)
                                        }
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                requireContext(),
                                                getString(R.string.account_deleted),
                                                Toast.LENGTH_LONG
                                            )
                                                .show()

                                        }
                                    }
                                } else {
                                    showRestoreDialog(user.userId)

                                }


                            } else {

                                val sessionManager = SessionManager(requireContext())
                                sessionManager.saveSession()
                                val sharedPreferences = requireContext().getSharedPreferences(
                                    "user_data",
                                    Context.MODE_PRIVATE
                                )
                                val editor = sharedPreferences.edit()
                                editor.putInt("user_id", user.userId)
                                editor.apply()


                                activity?.supportFragmentManager?.beginTransaction()?.apply {
                                    replace(
                                        R.id.main_activity_container,
                                        MainFragment()
                                    )
                                    addToBackStack(null)
                                    commit()

                                }
                            }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.no_user),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }

            btnRegister.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(
                        R.id.main_activity_container,
                        RegistrationFragment()
                    )
                    addToBackStack(null)
                    commit()
                }

            }
        }
    }


    private fun showRestoreDialog(userId: Int) {
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.user_account_deleted)
            .setMessage(R.string.choice)
            .setPositiveButton(R.string.recover) { _, _ ->
                restoreAccount(userId)
                val sessionManager = SessionManager(requireContext())
                sessionManager.saveSession()
                val sharedPreferences = requireContext().getSharedPreferences(
                    "user_data",
                    Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.putInt("user_id", userId)
                editor.apply()


                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(
                        R.id.main_activity_container,
                        MainFragment()
                    )
                    addToBackStack(null)
                    commit()

                }
            }
            .setNegativeButton(R.string.delete) { _, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        userDao.deleteUser(userId)
                    }
                }

            }
            .create().show()
    }

    private fun restoreAccount(userId: Int) {
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userDao.updateDeletedAt(userId, 0)
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}