package com.ecmelberk.taskerhttpserver.tasker.actions

import android.content.Context
import com.ecmelberk.taskerhttpserver.http.ServiceManager
import com.ecmelberk.taskerhttpserver.tasker.ActivityConfigTaskerNoOutputOrInput
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutputOrInput
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutputOrInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess

class ToggleServiceActionRunner : TaskerPluginRunnerActionNoOutputOrInput() {

    override fun run(context: Context, input: TaskerInput<Unit>): TaskerPluginResult<Unit> {
        ServiceManager.toggle(context)
        return TaskerPluginResultSucess()
    }

}

class ToggleServiceActionHelper(config: TaskerPluginConfig<Unit>) :
    TaskerPluginConfigHelperNoOutputOrInput<ToggleServiceActionRunner>(config) {

    override val runnerClass = ToggleServiceActionRunner::class.java

    override fun addToStringBlurb(input: TaskerInput<Unit>, blurbBuilder: StringBuilder) {
        blurbBuilder.append("Enable or disable the HTTP server.")
    }

}

class ToggleServiceAction :
    ActivityConfigTaskerNoOutputOrInput<ToggleServiceActionRunner, ToggleServiceActionHelper>() {

    override fun getNewHelper(config: TaskerPluginConfig<Unit>) = ToggleServiceActionHelper(config)

}