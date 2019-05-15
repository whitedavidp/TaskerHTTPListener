package com.ecmelberk.taskerhttpserver.http

import android.content.Context
import android.util.Log
import com.ecmelberk.taskerhttpserver.LOGNAME
import com.ecmelberk.taskerhttpserver.tasker.events.HTTPRequest
import com.ecmelberk.taskerhttpserver.tasker.events.HTTPRequestEvent
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery
import fi.iki.elonen.NanoHTTPD

class HTTP(host: String, port: Int, private val context: Context) : NanoHTTPD(host, port) {

    override fun serve(session: IHTTPSession?): Response {
        val files = mutableMapOf<String, String>()
        var body = ""

        Log.i(LOGNAME, "Got request to ${session?.uri}")

        if (session?.method == Method.POST) {
            session.parseBody(files)
            body = session.queryParameterString // this gets the body for some reason
        }

        HTTPRequestEvent::class.java.requestQuery(context, HTTPRequest(session?.uri, body))
        return newFixedLengthResponse("")
    }

}