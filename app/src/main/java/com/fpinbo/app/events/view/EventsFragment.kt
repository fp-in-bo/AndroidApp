package com.fpinbo.app.events.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.fpinbo.app.R
import com.fpinbo.app.events.Error
import com.fpinbo.app.events.Events
import com.fpinbo.app.events.EventsViewModel
import com.fpinbo.app.events.Loading
import com.fpinbo.app.utils.exhaustive
import com.fpinbo.app.utils.hide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.events_fragment.*

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, {

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
        list.adapter = EventsAdapter(data.events) { event, itemView ->
            val destination =
                EventsFragmentDirections.actionHomeToEventFragment().apply { setEvent(event) }
            val extras = FragmentNavigatorExtras(
                itemView to "shared_element_container"
            )
            findNavController().navigate(destination, extras)
            viewModel.trackSelectItem(event)
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
