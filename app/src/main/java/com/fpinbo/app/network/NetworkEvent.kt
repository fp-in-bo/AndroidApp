package com.fpinbo.app.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkEvent(
    @SerialName("title") val title: String,
    @SerialName("speaker") val speaker: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("description") val description: String
)