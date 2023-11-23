package com.example.kpfu_itis_android2023

import android.app.AlertDialog

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.kpfu_itis_android2023.fragments.CoroutinesFragment
import com.example.kpfu_itis_android2023.fragments.HomeFragment
import com.example.kpfu_itis_android2023.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.IntentFilter
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    private val notificationPermissionCode = 123
    private var notificationPermissionDeniedCount = 0
    private var isAirplaneModeOn = false
    private var airplaneModeReceiver: BroadcastReceiver? = null
    private var airplaneModeChangeListener: ((Boolean) -> Unit)? = null

    fun setAirplaneModeChangeListener(listener: (Boolean) -> Unit) {
        airplaneModeChangeListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkNotificationPermission()
        }
        onNewIntent(getIntent())


        findViewById<BottomNavigationView>(R.id.main_bottom_navigation)?.let {
            it.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.main_home -> {
                        replaceFragment(HomeFragment())
                        true
                    }

                    R.id.main_settings -> {
                        replaceFragment(SettingsFragment())
                        true
                    }

                    R.id.main_coroutines -> {
                        replaceFragment(CoroutinesFragment())
                        true
                    }

                    else -> false
                }
            }

        }
        replaceFragment(HomeFragment())
        registerAirplaneModeReceiver()

        isAirplaneModeOn = isAirplaneModeOn()
        registerAirplaneModeReceiver()


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.getStringExtra("OPEN_FRAGMENT")) {
            OPEN_FRAGMENT_HOME -> replaceFragment(HomeFragment())
            OPEN_FRAGMENT_TOAST -> Toast.makeText(applicationContext, R.string.done, Toast.LENGTH_LONG).show()
            OPEN_FRAGMENT_SETTINGS -> replaceFragment(SettingsFragment())
            else -> {/* обработка ошибки или других случаев */}
        }


    }


    private fun registerAirplaneModeReceiver() {
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    isAirplaneModeOn = isAirplaneModeOn()
                    updateUIBasedOnAirplaneMode(isAirplaneModeOn)
                    updateUIBasedOnAirplaneModeCoroutines(isAirplaneModeOn)

                }
            }
        }
        registerReceiver(airplaneModeReceiver, filter)
    }


    fun isAirplaneModeOn(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.getInt(contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) != 0
        } else {
            Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        }
    }


    private fun updateUIBasedOnAirplaneMode(isAirplaneMode: Boolean) {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_activity_container)
        if (fragment is HomeFragment) {
            fragment.updateUIBasedOnAirplaneMode(isAirplaneMode)
        }
        airplaneModeChangeListener?.invoke(isAirplaneMode)
    }

    private fun updateUIBasedOnAirplaneModeCoroutines(isAirplaneMode: Boolean) {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_activity_container)
        if (fragment is CoroutinesFragment) {
            fragment.updateUIBasedOnAirplaneModeInCoroutines(isAirplaneMode)
        }
        airplaneModeChangeListener?.invoke(isAirplaneMode)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeReceiver)
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_activity_container, fragment, fragment.javaClass.name)
            addToBackStack(null)
            commit()
        }
    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (notificationPermissionDeniedCount < 2) {
                requestNotificationPermission()
            } else {
                showPermissionDeniedMessage()
            }
        }
    }

    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            notificationPermissionCode
        )
    }

    private fun showPermissionDeniedMessage() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notification_off)
            .setMessage(R.string.turn_on_notification)
            .setPositiveButton(R.string.main_settings) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                openAppSettings()
            }
            .setNegativeButton(R.string.cancellation) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = android.net.Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == notificationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                notificationPermissionDeniedCount++
                checkNotificationPermission()
            }
        }
    }
    companion object {
        const val OPEN_FRAGMENT_HOME = "HOME"
        const val OPEN_FRAGMENT_TOAST = "Toast"
        const val OPEN_FRAGMENT_SETTINGS = "Settings"
    }

}