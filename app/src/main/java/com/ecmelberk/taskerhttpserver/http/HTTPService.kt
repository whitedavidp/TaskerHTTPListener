package com.ecmelberk.taskerhttpserver.http

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ecmelberk.taskerhttpserver.*
import fi.iki.elonen.NanoHTTPD

class HTTPService : Service() {

    var http: HTTP? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == START) {
            var host: String
            var port: Int

            PreferenceManager.getDefaultSharedPreferences(this).apply {
                host = getString("host", DEFAULT_HOST)!!
                port = getInt("port", DEFAULT_PORT)
            }

            http = HTTP(host, port, this)
            http?.start(NanoHTTPD.SOCKET_READ_TIMEOUT)
            Log.i(LOGNAME, "Server should be started about now")

            startForeground(1337, NotificationCompat.Builder(this, NOTIFICATION).apply {
                setContentTitle("Tasker HTTP Server")
                setContentText("Running. You can safely disable this notification.")
                setSmallIcon(R.drawable.ic_http_black_24dp)
                setContentIntent(
                    PendingIntent.getActivity(
                        this@HTTPService,
                        0,
                        Intent(this@HTTPService, MainActivity::class.java),
                        0
                    )
                )
            }.build())

            ServiceManager.running = true
        } else if (intent.action == STOP) {
            stopForeground(true)
            stopSelf()

            ServiceManager.running = false
        }

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        http?.stop()
        http = null

        Log.i(LOGNAME, "Server should be stopped about now")
    }

}
