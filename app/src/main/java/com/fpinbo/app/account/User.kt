package com.fpinbo.app.account

import android.net.Uri

data class User(
    val displayName: String?,
    val mail: String?,
    val avatarUrl: Uri?
)