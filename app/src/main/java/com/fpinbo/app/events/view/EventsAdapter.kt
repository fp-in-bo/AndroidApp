package com.fpinbo.app.events.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.fpinbo.app.R
import com.fpinbo.app.entities.Event

class EventsAdapter(
    private val data: List<Event>,
    private val listener: (Event, View, View, View, View) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position], listener)

    override fun getItemId(position: Int) = data[position].id.toLong()
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val speaker: TextView = itemView.findViewById(R.id.speaker)

    fun bind(
        event: Event,
        listener: (Event, View, View, View, View) -> Unit
    ) {

        val eventId = event.id

        ViewCompat.setTransitionName(title, "title_$eventId")
        ViewCompat.setTransitionName(image, "image_$eventId")
        ViewCompat.setTransitionName(speaker, "speaker_$eventId")

        title.text = event.title
        image.load(event.imageUrl)
        speaker.text = event.speaker

        itemView.setOnClickListener {
            listener(
                event, title, image, speaker, itemView
            )
        }
    }
}