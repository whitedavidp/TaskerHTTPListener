package com.ecmelberk.taskerhttpserver.tasker.events

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.ecmelberk.taskerhttpserver.R
import com.ecmelberk.taskerhttpserver.http.ServiceManager
import com.ecmelberk.taskerhttpserver.tasker.ActivityConfigTasker
import com.joaomgcd.taskerpluginlibrary.condition.TaskerPluginRunnerConditionEvent
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultCondition
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionSatisfied
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionUnsatisfied
import kotlinx.android.synthetic.main.activity_http_request_event.*

class HTTPRequestRunner : TaskerPluginRunnerConditionEvent<HTTPRequestFilter, HTTPRequest, HTTPRequest>() {

    override fun getSatisfiedCondition(
        context: Context,
        input: TaskerInput<HTTPRequestFilter>,
        update: HTTPRequest?
    ): TaskerPluginResultCondition<HTTPRequest> {
        if (!ServiceManager.running)
            return TaskerPluginResultConditionUnsatisfied()

        if (update?.requestPath != input.regular.requestPath)
            return TaskerPluginResultConditionUnsatisfied()

        if (!input.regular.requestBodyContains.isNullOrEmpty())
            if (!update?.requestBody?.contains(input.regular.requestBodyContains.toString())!!)
                return TaskerPluginResultConditionUnsatisfied()

        if (!input.regular.requestMethod.isNullOrEmpty())
            if (update?.requestMethod != input.regular.requestMethod)
                return TaskerPluginResultConditionUnsatisfied()

        return TaskerPluginResultConditionSatisfied(context, update)
    }

}


class HTTPRequestHelper(config: TaskerPluginConfig<HTTPRequestFilter>) :
    TaskerPluginConfigHelper<HTTPRequestFilter, HTTPRequest, HTTPRequestRunner>(config) {

    override val inputClass = HTTPRequestFilter::class.java
    override val outputClass = HTTPRequest::class.java
    override val runnerClass = HTTPRequestRunner::class.java

}


class HTTPRequestEvent : ActivityConfigTasker<HTTPRequestFilter, HTTPRequest, HTTPRequestRunner, HTTPRequestHelper>() {

    override val layoutResId = R.layout.activity_http_request_event
    override val inputForTasker: TaskerInput<HTTPRequestFilter>
        get() {
            var requestPath = request_path_edit.text.toString()
            if (!requestPath.startsWith("/"))
                requestPath = "/$requestPath"

            return TaskerInput(
                HTTPRequestFilter(
                    requestPath,
                    request_body_edit.text.toString(),
                    request_method_spinner.selectedItem.toString()
                )
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        request_method_spinner.adapter =
            ArrayAdapter.createFromResource(context, R.array.http_methods, android.R.layout.simple_spinner_item).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
    }

    override fun getNewHelper(config: TaskerPluginConfig<HTTPRequestFilter>) = HTTPRequestHelper(config)

    override fun assignFromInput(input: TaskerInput<HTTPRequestFilter>) {
        request_path_edit.setText(input.regular.requestPath)
        request_body_edit.setText(input.regular.requestBodyContains)

        request_method_spinner.post {
            request_method_spinner.setSelection(resources.getStringArray(R.array.http_methods).indexOf(input.regular.requestMethod))
        }
    }


}

