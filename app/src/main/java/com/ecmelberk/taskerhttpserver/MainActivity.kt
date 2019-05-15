package com.ecmelberk.taskerhttpserver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.ecmelberk.taskerhttpserver.http.ServiceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isRunning = false
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        pref = PreferenceManager.getDefaultSharedPreferences(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        title = "TaskerHTTPServer ${BuildConfig.VERSION_NAME}"

        ServiceManager.onRunningChange {
            isRunning = ServiceManager.running

            status_text.text =
                if (isRunning) "Service Running"
                else "Service Not Running"
        }

        pref.apply {
            host_edit.setText(getString("host", DEFAULT_HOST))
            port_edit.setText(getInt("port", DEFAULT_PORT).toString())
        }

        toggle_button.setOnClickListener {
            pref.edit {
                putString("host", host_edit.text.toString())
                putInt("port", port_edit.text.toString().toInt())

                putBoolean("enabled", !isRunning)
                commit()
            }

            ServiceManager.toggle(this)
        }

        createNotificationChannnel()
    }

    private fun createNotificationChannnel() {
        if (Build.VERSION.SDK_INT < 26) return

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (nm.getNotificationChannel(NOTIFICATION) != null) return

        val channel = NotificationChannel(NOTIFICATION, "Service Notification", NotificationManager.IMPORTANCE_LOW)

        nm.createNotificationChannel(channel)
    }

}
