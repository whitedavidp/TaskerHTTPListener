package com.ecmelberk.taskerhttpserver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecmelberk.taskerhttpserver.http.ServiceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ServiceManager.onRunningChange {
            isRunning = ServiceManager.running

            status_text.text =
                if (isRunning) "Service Running"
                else "Service Not Running"
        }

        toggle_button.setOnClickListener {
            ServiceManager.toggle(this)
        }

        createNotificationChannnel()
    }

    private fun createNotificationChannnel() {
        if (Build.VERSION.SDK_INT < 26) return

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (nm.getNotificationChannel(NOTIF_CHANNEL) != null) return

        val channel = NotificationChannel(NOTIF_CHANNEL, "Service Notification", NotificationManager.IMPORTANCE_LOW)

        nm.createNotificationChannel(channel)
    }

}
