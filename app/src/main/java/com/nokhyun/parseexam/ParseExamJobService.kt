package com.nokhyun.parseexam

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ParseExamJobService : JobService() {

    private val serviceRepository: ServiceRepository = ServiceRepositoryImpl()
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private lateinit var notificationManager: NotificationManager

    // 문서 샘플
    private val notification: Notification? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(applicationContext, "NOTIFICATION_CHANNEL_ID")
                .setContentTitle("My user-initiated data transfer job")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("Job is running")
                .build()
        } else {
            null
        }
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(applicationContext, "ChannelId1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("테스트 타이틀")
            .setContentText("드가자!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("ChannelId1", "ChannelName", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(30, builder.build())
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val channel = NotificationChannel("NOTIFICATION_CHANNEL_ID", "NOTIFICATION_CHANNEL_ID_ChannelName", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

            setNotification(params!!, 21, notification!!, JOB_END_NOTIFICATION_POLICY_DETACH)
        }

        serviceScope.launch {
            delay(5_000)
            serviceRepository.fetchTodo().collectLatest {
                log("onStartJob fetchTodo: $it")
                createNotification()
                jobFinished(params, false)
            }
        }
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        return false
    }

    private fun log(msg: String) {
        Log.e("ParseExamJobService", msg)
    }


}