package com.example.kpfu_itis_android2023.fragments


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.kpfu_itis_android2023.MainActivity
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.FragmentCoroutinesBinding
import kotlinx.coroutines.*

class CoroutinesFragment : Fragment() {

//    private lateinit var seekBar: SeekBar
//    private lateinit var asyncCheckBox: CheckBox
//    private lateinit var stopOnBackgroundCheckBox: CheckBox
//    private lateinit var executeButton: Button
    private var _viewBinding: FragmentCoroutinesBinding? = null
    private val viewBinding: FragmentCoroutinesBinding
        get() = _viewBinding!!
    private var job: Job? = null
    private var unfinishedCoroutines = 0
    private var stopOnBackground = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentCoroutinesBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
//            seekBar = viewBinding.seekBar
//            asyncCheckBox = viewBinding.checkBoxAsync
//            stopOnBackgroundCheckBox = viewBinding.checkBoxStopOnBackground
//            executeButton = viewBinding.buttonExecute
//            Coroutine.count = seekBar.progress

            (requireActivity() as MainActivity).apply {
                if (isAirplaneModeOn()) {
                    buttonExecute.isEnabled = false
                } else {
                    buttonExecute.isEnabled = true
                    buttonExecute.setOnClickListener {
                        unfinishedCoroutines = seekBar.progress
                        stopOnBackground = checkBoxStopOnBackground.isChecked
                        job = lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                repeat(seekBar.progress) {
                                    if (checkBoxAsync.isChecked) {
                                        launch {
                                            delay(1000)
                                            println("Coroutine $it finished.")
                                            unfinishedCoroutines--
                                        }
                                    } else {
                                        delay(1000)
                                        println("Coroutine $it finished.")
                                        unfinishedCoroutines--
                                    }
                                }
                            }
                        }.also {
                            it.invokeOnCompletion {
                                if (it == null) {
                                    showJobDoneNotification()
                                    println("Success")
                                } else if (it is CancellationException) {
                                    println("Cancelled $unfinishedCoroutines")
                                }
                                job = null
                            }
                        }

                    }
                }
            }
        }
    }


    fun updateUIBasedOnAirplaneModeInCoroutines(isAirplaneMode: Boolean = false) {
        viewBinding.buttonExecute.isEnabled = !isAirplaneMode
    }


    override fun onStop() {
        if (stopOnBackground) {
            job?.cancel(
                "The application is closed"
            )
        }
        super.onStop()
    }

    private fun showJobDoneNotification() {
        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager?

        if (notificationManager != null) {
            val notificationId = 2
            val channelId = "coroutine_channel"
            val channelName = "Coroutine Channel"

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.FOREGROUND_SERVICE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                val builder = NotificationCompat.Builder(requireContext(), channelId)
                    .setContentTitle("Job Done")
                    .setContentText("my job here is done")
                    .setSmallIcon(R.drawable.notifications)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                notificationManager.notify(notificationId, builder.build())
            }
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}