package com.fpinbo.app.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

private const val APP_STATUS = "APP_STATUS"
private const val LAST_SCREEN = "LAST_SCREEN"

class LifeCycleLogger @Inject constructor() : Application.ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {

    private val instance = FirebaseCrashlytics.getInstance()


    override fun onStart(owner: LifecycleOwner) {
        instance.log("Foreground")
        instance.setCustomKey(APP_STATUS, "Foreground")
    }

    override fun onStop(owner: LifecycleOwner) {
        instance.log("Background")
        instance.setCustomKey(APP_STATUS, "Background")
    }

    private val fragmentListener = object :
        FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            val fragmentLog = "Created ${f.javaClass.simpleName}: ${f.arguments}"
            instance.log(fragmentLog)
            instance.setCustomKey(LAST_SCREEN, fragmentLog)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        val activityLog = "Created ${activity.componentName}: ${activity.intent}"
        instance.setCustomKey(LAST_SCREEN, activityLog)

        instance.log(activityLog)

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