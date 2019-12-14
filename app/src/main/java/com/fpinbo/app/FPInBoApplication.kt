package com.fpinbo.app

import android.app.Application
import android.content.Context
import com.fpinbo.app.inject.AppComponent
import com.fpinbo.app.inject.AppModule
import com.fpinbo.app.inject.DaggerAppComponent

class FPInBoApplication : Application() {

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            val app = context.applicationContext as FPInBoApplication
            return app.appComponent
        }
    }


    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}