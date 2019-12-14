package com.fpinbo.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fpinbo.app.events.EventsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val eventsFragment = EventsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, eventsFragment)
                .commit()
        }
    }
}
