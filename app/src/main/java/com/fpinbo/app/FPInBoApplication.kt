package com.fpinbo.app

import android.app.Application
import android.content.Context
import com.fpinbo.app.inject.*
import javax.inject.Provider

class FPInBoApplication : Application(), HasSubComponentBuilders {

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

    override fun subComponentBuilders(): Map<Class<*>, Provider<SubComponentBuilder<*>>> =
        appComponent.subComponentBuilders()
}