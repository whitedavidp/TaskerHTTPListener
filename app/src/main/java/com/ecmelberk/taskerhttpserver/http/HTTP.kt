package com.ecmelberk.taskerhttpserver.http

import android.content.Context
import android.util.Log
import com.ecmelberk.taskerhttpserver.LOGNAME
import com.ecmelberk.taskerhttpserver.tasker.events.HTTPRequest
import com.ecmelberk.taskerhttpserver.tasker.events.HTTPRequestEvent
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery
import fi.iki.elonen.NanoHTTPD
import java.nio.charset.Charset

class HTTP(host: String, port: Int, private val context: Context) : NanoHTTPD(host, port) {

    override fun serve(session: IHTTPSession?): Response {
        Log.i(LOGNAME, "Got request to ${session?.uri}")

        val contentLength = Integer.parseInt(session?.headers?.get("content-length"))
        val buffer = ByteArray(contentLength)
        session?.inputStream?.read(buffer, 0, contentLength)

        val body = buffer.toString(Charset.defaultCharset())
        Log.d(LOGNAME, "body is $body")

        HTTPRequestEvent::class.java.requestQuery(context, HTTPRequest(session?.uri, body, session?.method?.name))
        return newFixedLengthResponse("")
    }

}