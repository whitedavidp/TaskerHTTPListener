package com.ecmelberk.taskerhttpserver.http

import fi.iki.elonen.NanoHTTPD

class HTTP : NanoHTTPD("0.0.0.0", 5556) {

    override fun serve(session: IHTTPSession?): Response {
        return newFixedLengthResponse("hello, world!")
    }

}