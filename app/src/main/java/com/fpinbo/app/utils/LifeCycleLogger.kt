package com.fpinbo.app.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.crashlytics.android.Crashlytics
import javax.inject.Inject

private const val APP_STATUS = "APP_STATUS"
private const val LAST_SCREEN = "LAST_SCREEN"

class LifeCycleLogger @Inject constructor() : Application.ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        Crashlytics.log("Foreground")
        Crashlytics.setString(APP_STATUS, "Foreground")
    }

    override fun onStop(owner: LifecycleOwner) {
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