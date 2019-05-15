package com.ecmelberk.taskerhttpserver.http

import android.content.Context
import android.util.Log
import com.ecmelberk.taskerhttpserver.tasker.events.HTTPRequest
import com.ecmelberk.taskerhttpserver.tasker.events.HTTPRequestEvent
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery
import fi.iki.elonen.NanoHTTPD

class HTTP(private val context: Context) : NanoHTTPD("0.0.0.0", 5556) {

    override fun serve(session: IHTTPSession?): Response {
        Log.i("TaskerHTTPServerPlugin", "Got request to ${session?.uri}")
        HTTPRequestEvent::class.java.requestQuery(context, HTTPRequest(session?.uri))
        return newFixedLengthResponse("hello, world!")
    }

}