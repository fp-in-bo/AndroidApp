package com.fpinbo.app.events.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.fpinbo.app.R
import com.fpinbo.app.events.Error
import com.fpinbo.app.events.Events
import com.fpinbo.app.events.EventsViewModel
import com.fpinbo.app.events.Loading
import com.fpinbo.app.events.inject.EventsModule
import com.fpinbo.app.events.inject.EventsSubComponent
import com.fpinbo.app.utils.exhaustive
import com.fpinbo.app.utils.hide
import com.fpinbo.app.utils.subComponentBuilder
import kotlinx.android.synthetic.main.events_fragment.*
import javax.inject.Inject


class EventsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: EventsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        val eventsSubComponent =
            context.subComponentBuilder<EventsSubComponent.Builder>()
                .eventsModule(EventsModule())
                .build()
        eventsSubComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {

            TransitionManager.beginDelayedTransition(root)

            exhaustive..when (it) {
                Loading -> bindLoading()
                is Events -> bindEvents(it)
                is Error -> bindError(it)
            }
        })
    }


    private fun bindLoading() {
        loader.isVisible = true
        hide(error, list)
    }


    private fun bindEvents(data: Events) {
        list.isVisible = true
        list.adapter = EventsAdapter(data.events) { event, tile, image, speaker, itemView ->
            val eventId = event.id
            val destination = EventsFragmentDirections.actionHomeToEventFragment(event)
            val extras = FragmentNavigatorExtras(
                tile to "title_$eventId",
                image to "image_$eventId",
                speaker to "speaker_$eventId"
            )
            (exitTransition as Transition).excludeTarget(itemView, true)

            findNavController().navigate(destination, extras)
        }
        postponeEnterTransition()
        list.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        hide(loader, error)
    }

    private fun bindError(data: Error) {
        error.isVisible = true
        error.text = data.message
        hide(loader, list)
    }
}
