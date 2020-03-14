package com.fpinbo.app.network

import com.fpinbo.app.entities.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkEvent(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("speaker") val speaker: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("description") val description: String,
    @SerialName("videoUrl") val videoUrl: String? = null
)

fun NetworkEvent.toEntity(): Event = Event(
    id = id,
    title = title,
    speaker = speaker,
    imageUrl = imageUrl,
    description = description,
    videoUrl = videoUrl,
    shareUrl = "https://fp-in-bo.github.io/$id.html"
)