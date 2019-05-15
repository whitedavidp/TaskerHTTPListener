package com.ecmelberk.taskerhttpserver.tasker.events

import com.ecmelberk.taskerhttpserver.R
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable


@TaskerInputRoot
class HTTPRequestFilter @JvmOverloads constructor(

    @field:TaskerInputField("requestPath", labelResId = R.string.request_path) val requestPath: String? = null

)

@TaskerInputRoot
@TaskerOutputObject
class HTTPRequest @JvmOverloads constructor(

    @field:TaskerInputField("requestPath") @get:TaskerOutputVariable("httprequest", R.string.request_path, R.string.request_path_desc) val requestPath: String? = null

)