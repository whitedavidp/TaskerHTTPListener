package com.ecmelberk.taskerhttpserver.http

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ecmelberk.taskerhttpserver.NOTIF_CHANNEL
import com.ecmelberk.taskerhttpserver.R
import com.ecmelberk.taskerhttpserver.START_SERVICE
import com.ecmelberk.taskerhttpserver.STOP_SERVICE
import fi.iki.elonen.NanoHTTPD

class HTTPService : Service() {

    val http = HTTP()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == START_SERVICE) {
            startForeground(1337, NotificationCompat.Builder(this, NOTIF_CHANNEL).apply {
                setContentTitle("Tasker HTTP Server")
                setContentText("Running. You can safely disable this notification.")
                setSmallIcon(R.drawable.ic_http_black_24dp)
            }.build())

            ServiceManager.running = true
            http.start(NanoHTTPD.SOCKET_READ_TIMEOUT)
            Log.i("TaskerHTTPServerPlugin", "Server should be started about now")
        } else if (intent.action == STOP_SERVICE) {
            ServiceManager.running = false

            stopForeground(true)
            stopSelf()
        }

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        http.stop()
        Log.i("TaskerHTTPServerPlugin", "Server should be stopped about now")
    }

}
