package com.fpinbo.app.account.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.fpinbo.app.R

class NotLoggedView : FrameLayout {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val logIn: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.view_not_logged, this, true)

        logIn = findViewById(R.id.user_login)
    }

    fun bind(
        onLogin: () -> Unit
    ) {
        logIn.setOnClickListener {
            onLogin()
        }
    }
}