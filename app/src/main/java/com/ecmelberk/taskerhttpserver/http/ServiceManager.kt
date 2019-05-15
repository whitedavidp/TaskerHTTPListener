package com.ecmelberk.taskerhttpserver.http

import android.content.Context
import android.content.Intent
import android.os.Build
import com.ecmelberk.taskerhttpserver.START_SERVICE
import com.ecmelberk.taskerhttpserver.STOP_SERVICE

internal object ServiceManager {

    var running = false
        set(running) {
            field = running

            if (callback != null)
                callback?.invoke()
        }

    private var callback: (()->Unit)? = null

    fun start(context: Context, foreground: Boolean = false) {
        val startIntent = Intent(context, HTTPService::class.java)
        startIntent.action = START_SERVICE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && foreground)
            context.startForegroundService(startIntent)
        else
            context.startService(startIntent)
    }

    fun stop(context: Context) {
        val stopIntent = Intent(context, HTTPService::class.java)
        stopIntent.action = STOP_SERVICE

        context.startService(stopIntent)
    }

    fun onRunningChange(callback: ()->Unit) {
        this.callback = callback

        callback()
    }

    fun toggle(context: Context) {
        if (this.running)
            ServiceManager.stop(context)
        else
            ServiceManager.start(context)
    }

}