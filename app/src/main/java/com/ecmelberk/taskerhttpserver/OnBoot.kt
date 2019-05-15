package com.ecmelberk.taskerhttpserver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.ecmelberk.taskerhttpserver.http.ServiceManager

class OnBoot : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enabled", false))
            ServiceManager.start(context, true)
    }

}
