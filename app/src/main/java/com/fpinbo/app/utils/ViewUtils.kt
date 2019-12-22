package com.fpinbo.app.utils

import android.view.View
import androidx.core.view.isVisible

fun hide(vararg views: View) = views.forEach { it.isVisible = false }
