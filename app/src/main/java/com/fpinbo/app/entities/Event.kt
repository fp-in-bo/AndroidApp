package com.fpinbo.app.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val id: Int,
    val title: String,
    val speaker: String,
    val imageUrl: String,
    val description: String,
    val videoUrl: String?,
    val shareUrl: String
) : Parcelable