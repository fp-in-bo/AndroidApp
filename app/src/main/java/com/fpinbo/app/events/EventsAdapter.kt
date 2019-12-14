package com.fpinbo.app.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.fpinbo.app.R

class EventsAdapter(
    private val data: List<Event>
) : RecyclerView.Adapter<ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemId(position: Int) = data[position].hashCode().toLong()
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val speaker: TextView = itemView.findViewById(R.id.speaker)

    fun bind(event: Event) {
        title.text = event.title
        image.load(event.imageUrl)
        speaker.text = event.speaker
    }
}