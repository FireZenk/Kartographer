package org.firezenk.kartographer.animations

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import org.firezenk.kartographer.annotations.Monitor

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 08/01/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class ContextMonitor : Monitor(), Application.ActivityLifecycleCallbacks {

    private lateinit var observer: (Any) -> Unit

    override fun onContextChanged(block: (Any) -> Unit) {
        observer = block
    }

    override fun onContextCaptured(context: Any) {
        observer(context)
    }
    
    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) = onActivityCaptured(activity)

    override fun onActivityStarted(activity: Activity?) = onActivityCaptured(activity)

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, p1: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivityCreated(activity: Activity?, p1: Bundle?) = onActivityCaptured(activity)

    private fun onActivityCaptured(activity: Activity?) {
        if (activity?.callingActivity == null) {
            onContextCaptured(activity as Context)
        }
    }
}