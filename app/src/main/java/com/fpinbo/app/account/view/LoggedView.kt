package com.fpinbo.app.account.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import coil.api.load
import com.fpinbo.app.R
import com.fpinbo.app.account.User

class LoggedView : FrameLayout {

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

    private val avatar: ImageView
    private val name: TextView
    private val mail: TextView
    private val logOut: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.view_logged, this, true)

        avatar = findViewById(R.id.user_avatar)
        name = findViewById(R.id.user_name)
        mail = findViewById(R.id.user_mail)
        logOut = findViewById(R.id.user_logout)
    }

    fun bind(
        user: User,
        onLogout: () -> Unit
    ) {
        avatar.load(user.avatarUrl)
        if (user.displayName.isNullOrBlank()) {
            name.isVisible = false
        } else {
            name.isVisible = true
            name.text = user.displayName
        }
        mail.text = user.mail
        logOut.setOnClickListener {
            onLogout()
        }
    }
}