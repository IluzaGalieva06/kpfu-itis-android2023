package com.example.kpfu_itis_android2023.fragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.kpfu_itis_android2023.MainActivity
import com.example.kpfu_itis_android2023.MainActivity.Companion.OPEN_FRAGMENT_HOME
import com.example.kpfu_itis_android2023.MainActivity.Companion.OPEN_FRAGMENT_SETTINGS
import com.example.kpfu_itis_android2023.MainActivity.Companion.OPEN_FRAGMENT_TOAST
import com.example.kpfu_itis_android2023.Notifications
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val notificationChannelId = "my_channel"
    private val notificationChannelName = "My Channel"
    private val notificationId = 101
    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding: FragmentHomeBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            (requireActivity() as MainActivity).apply {
                if (isAirplaneModeOn()) {
                    showNotificationButton.isEnabled = false
                } else {
                    showNotificationButton.isEnabled = true
                    showNotificationButton.setOnClickListener {
                        showNotification()
                    }
                }
            }
        }

    }

    fun updateUIBasedOnAirplaneMode(isAirplaneMode: Boolean = false) {
        viewBinding.showNotificationButton.isEnabled = !isAirplaneMode
    }


    private fun showNotification() {
        val title = viewBinding.notificationTitleEditText.text.toString()
        val message = viewBinding.notificationMessageEditText.text.toString()

        val importance = Notifications.importance
        val visibility = Notifications.visibility
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("OPEN_FRAGMENT", OPEN_FRAGMENT_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notificationBuilder =
            NotificationCompat.Builder(requireContext(), notificationChannelId)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(getPriority(importance))
                .setVisibility(getPrivacy(visibility))
                .setContentIntent(pendingIntent)
        if (Notifications.isBigText) {
            val bigTextStyle = NotificationCompat.BigTextStyle()
                .bigText(message)
            notificationBuilder.setStyle(bigTextStyle)
        }


        if (Notifications.showActions) {


            val toastIntent = Intent(requireContext(), MainActivity::class.java)
            toastIntent.putExtra("OPEN_FRAGMENT", OPEN_FRAGMENT_TOAST)
            val toastPendingIntent = PendingIntent.getActivity(
                requireContext(),
                1,
                toastIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val action1 = NotificationCompat.Action.Builder(
                null,
                "Сообщение",
                toastPendingIntent
            ).build()

            notificationBuilder.addAction(action1)
            val settingIntent = Intent(requireContext(), MainActivity::class.java)
            settingIntent.putExtra("OPEN_FRAGMENT", OPEN_FRAGMENT_SETTINGS)

            val settingPendingIntent = PendingIntent.getActivity(
                requireContext(),
                2,
                settingIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val action2 = NotificationCompat.Action.Builder(
                null,
                "Настройки",
                settingPendingIntent
            ).build()

            notificationBuilder.addAction(action2)
        }
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun getPriority(importance: String): Int {
        return when (importance) {
            "Medium" -> NotificationCompat.PRIORITY_DEFAULT
            "High" -> NotificationCompat.PRIORITY_HIGH
            "Urgent" -> NotificationCompat.PRIORITY_MAX
            else -> NotificationCompat.PRIORITY_DEFAULT
        }
    }

    private fun getPrivacy(privacy: String): Int {
        return when (privacy) {
            "Private" -> Notification.VISIBILITY_PRIVATE
            "Secret" -> Notification.VISIBILITY_SECRET
            else -> Notification.VISIBILITY_PUBLIC
        }
    }


}