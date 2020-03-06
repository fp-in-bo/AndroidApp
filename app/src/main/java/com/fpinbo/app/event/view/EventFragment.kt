package com.fpinbo.app.event.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.fpinbo.app.R
import com.fpinbo.app.event.Data
import com.fpinbo.app.event.Error
import com.fpinbo.app.event.EventViewModel
import com.fpinbo.app.event.inject.EventModule
import com.fpinbo.app.event.inject.EventSubComponent
import com.fpinbo.app.utils.exhaustive
import com.fpinbo.app.utils.subComponentBuilder
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.event_fragment.*
import javax.inject.Inject

class EventFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: EventViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        val eventSubComponent =
            context.subComponentBuilder<EventSubComponent.Builder>()
                .eventModule(
                    EventModule(
                        requireActivity().intent,
                        navArgs<EventFragmentArgs>().value
                    )
                )
                .build()
        eventSubComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext()).apply {
            scrimColor = ContextCompat.getColor(requireContext(), R.color.transparent)
            interpolator = OvershootInterpolator()
            duration = 600
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {

            exhaustive..when (it) {
                is Data -> bindData(it)
                is Error -> bindError(it)
            }

        })
    }

    private fun bindData(data: Data) {
        val event = data.event
        image.load(event.imageUrl)
        title.text = event.title
        speaker.text = event.speaker
        description.text = event.description

    }

    private fun bindError(error: Error) {
        description.text = error.message
    }
}