package com.ecmelberk.taskerhttpserver.tasker.events

import android.content.Context
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
    override val inputForTasker
        get() = TaskerInput(
            HTTPRequestFilter(
                request_path_edit.text.toString(),
                request_body_edit.text.toString()
            )
        )

    override fun getNewHelper(config: TaskerPluginConfig<HTTPRequestFilter>) = HTTPRequestHelper(config)

    override fun assignFromInput(input: TaskerInput<HTTPRequestFilter>) {
        request_path_edit.setText(input.regular.requestPath)
        request_body_edit.setText(input.regular.requestBodyContains)
    }


}

