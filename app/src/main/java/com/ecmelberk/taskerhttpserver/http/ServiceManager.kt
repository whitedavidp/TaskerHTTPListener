package com.ecmelberk.taskerhttpserver.http

import android.content.Context
import android.content.Intent
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

    fun start(context: Context) {
        val startIntent = Intent(context, HTTPService::class.java)
        startIntent.action = START_SERVICE

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

}