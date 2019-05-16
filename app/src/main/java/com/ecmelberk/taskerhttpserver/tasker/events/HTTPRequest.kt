package com.ecmelberk.taskerhttpserver.tasker.events

import com.ecmelberk.taskerhttpserver.R
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable


@TaskerInputRoot
class HTTPRequestFilter @JvmOverloads constructor(

    @field:TaskerInputField("requestPath", labelResId = R.string.request_path) val requestPath: String? = null,
    @field:TaskerInputField("requestBody", labelResId = R.string.request_body_contains) val requestBodyContains: String? = null,
    @field:TaskerInputField("requestMethod", labelResId = R.string.request_method) val requestMethod: String? = null

)

@TaskerInputRoot
@TaskerOutputObject
class HTTPRequest @JvmOverloads constructor(

    @field:TaskerInputField("requestPath") @get:TaskerOutputVariable("httprequest", R.string.request_path, R.string.request_path_desc) val requestPath: String? = null,
    @field:TaskerInputField("requestBody") @get:TaskerOutputVariable("httpbody", R.string.request_body, R.string.request_body_desc) val requestBody: String? = null,
    @field:TaskerInputField("requestMethod") @get:TaskerOutputVariable("httpmethod", R.string.request_method, R.string.request_method_desc) val requestMethod: String? = null

)