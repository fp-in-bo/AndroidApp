package com.fpinbo.app.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.crashlytics.android.Crashlytics

private const val APP_STATUS = "APP_STATUS"
private const val LAST_SCREEN = "LAST_SCREEN"

class LifeCycleLogger : Application.ActivityLifecycleCallbacks, LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        Crashlytics.log("Foreground")
        Crashlytics.setString(APP_STATUS, "Foreground")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        Crashlytics.log("Background")
        Crashlytics.setString(APP_STATUS, "Background")
    }

    private val fragmentListener = object :
        FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            val fragmentLog = "Created ${f.javaClass.simpleName}: ${f.arguments}"
            Crashlytics.log(fragmentLog)
            Crashlytics.setString(LAST_SCREEN, fragmentLog)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        val activityLog = "Created ${activity.componentName}: ${activity.intent}"
        Crashlytics.setString(LAST_SCREEN, activityLog)

        Crashlytics.log(activityLog)

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                fragmentListener, true
            )
        }
    }

    override fun onActivityDestroyed(activity: Activity) {

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit


}