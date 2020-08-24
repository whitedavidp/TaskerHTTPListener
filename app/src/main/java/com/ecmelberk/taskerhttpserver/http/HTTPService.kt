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
import java.io.InputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory

class HTTPService : Service() {

    var http: HTTP? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == START) {
            var host: String
            var port: Int
            var ssl: Boolean

            PreferenceManager.getDefaultSharedPreferences(this).apply {
                host = getString("host", DEFAULT_HOST)!!
                port = getInt("port", DEFAULT_PORT)
                ssl = getBoolean("ssl", true)
            }

            http = HTTP(host, port, this)

            // followed Ravi Patel's solution here https://stackoverflow.com/questions/31270613/https-server-on-android-device-using-nanohttpd/35765334
            // remember to use KeyStore Explorer to make a BKS-V1 version to put in assets. make sure to use the passwords below or change
            // below to what matches yours
            if(ssl) {
                val keyStoreStream: InputStream = this.getAssets().open("keystore.bks")
                val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(keyStoreStream, "myKeyStorePass".toCharArray())
                val keyManagerFactory: KeyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                keyManagerFactory.init(keyStore, "myCertificatePass".toCharArray())
                http?.makeSecure(NanoHTTPD.makeSSLSocketFactory(keyStore, keyManagerFactory), null)
            }

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
