package com.fpinbo.app.event.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.api.load
import com.fpinbo.app.R
import com.fpinbo.app.event.inject.EventModule
import com.fpinbo.app.event.inject.EventSubComponent
import com.fpinbo.app.utils.subComponentBuilder
import kotlinx.android.synthetic.main.event_fragment.*

class EventFragment : Fragment() {

    private val args: EventFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        val eventSubComponent =
            context.subComponentBuilder<EventSubComponent.Builder>()
                .eventModule(EventModule())
                .build()
        eventSubComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val event = args.event
        val eventId = event.hashCode()

        ViewCompat.setTransitionName(title, "title_$eventId")
        ViewCompat.setTransitionName(image, "image_$eventId")
        ViewCompat.setTransitionName(speaker, "speaker_$eventId")

        image.load(event.imageUrl)
        title.text = event.title
        speaker.text = event.speaker
        description.text = event.description
    }
}