package com.fpinbo.app.events.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.fpinbo.app.R
import com.fpinbo.app.events.Error
import com.fpinbo.app.events.Events
import com.fpinbo.app.events.EventsViewModel
import com.fpinbo.app.events.Loading
import com.fpinbo.app.events.inject.EventsModule
import com.fpinbo.app.events.inject.EventsSubComponent
import com.fpinbo.app.utils.exhaustive
import com.fpinbo.app.utils.subComponentBuilder
import kotlinx.android.synthetic.main.events_fragment.*
import javax.inject.Inject

class EventsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: EventsViewModel

    override fun onAttach(context: Context) {
        val eventsSubComponent =
            context.subComponentBuilder<EventsSubComponent.Builder>()
                .eventsModule(EventsModule())
                .build()
        eventsSubComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EventsViewModel::class.java)

        viewModel.state.observe(this, Observer {

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
        list.adapter = EventsAdapter(data.events)
        hide(loader, error)

    }

    private fun bindError(data: Error) {
        error.isVisible = true
        error.text = data.message
        hide(loader, list)
    }

    private fun hide(vararg views: View) = views.forEach { it.isVisible = false }

}
