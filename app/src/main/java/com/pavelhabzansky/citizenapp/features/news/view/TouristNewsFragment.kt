package com.pavelhabzansky.citizenapp.features.news.view

import android.content.Context
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.databinding.FragmentTouristNewsBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.EventsAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vo.ScheduleViewObject
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.net.UnknownHostException

class TouristNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentTouristNewsBinding

    private val viewModel by sharedViewModel<NewsViewModel>()

    private val eventAdapter: EventsAdapter by lazy {
        EventsAdapter(onClick = this::onItemClick)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tourist_news, container, false)

        binding.newsSourceRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        binding.swipeContainer.setOnRefreshListener { viewModel.loadEvents(true) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        if (eventAdapter.items.isEmpty()) {
            viewModel.loadEvents(false)
            binding.swipeContainer.isRefreshing = true
        }
    }

    private fun registerEvents() {
        viewModel.touristNewsViewState.observe(this, Observer {
            updateViewState(event = it)
        })

        viewModel.newsErrorState.observe(this, Observer {
            when (it.t) {
                is UnknownHostException -> toast("Nedostupné připojení")
            }
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.EventsDownloadComplete -> viewModel.loadCachedEvents()
            is NewsViewState.SchedulesLoadedEvent -> updateList(event.schedules)
            is NewsViewState.NoConnectionEvent -> noConnectionAvailable()
            is NewsViewState.LocationPermissionNotGranted -> noLocationAvailable()
        }
    }

    private fun updateList(items: List<ScheduleViewObject>) {
        binding.swipeContainer.isRefreshing = false
        eventAdapter.updateItems(items)

        when (items.isEmpty()) {
            true -> noEventsAvailable()
            else -> showEvents()
        }
    }

    private fun noLocationAvailable() {
        binding.swipeContainer.isRefreshing = false
        binding.newsSourceRecycler.hide()
        binding.title.show()
        binding.title.text = getString(R.string.location_unavailable)
    }

    private fun noConnectionAvailable() {
        binding.swipeContainer.isRefreshing = false
        binding.newsSourceRecycler.hide()
        binding.title.show()
        binding.title.text = getString(R.string.no_connection)
    }

    private fun showEvents() {
        binding.title.hide()
        binding.newsSourceRecycler.show()
    }

    private fun noEventsAvailable() {
        binding.title.text = getString(R.string.tourist_no_events_available)
        binding.title.show()
        binding.newsSourceRecycler.hide()
    }

    private fun onItemClick(event: ScheduleViewObject) {
        val args = Bundle()
        args.putString(ARG_EVENT_DATA, event.toJson())
        event.bitmap?.let {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            args.putByteArray(ARG_EVENT_IMAGE, stream.toByteArray())
        }
        findParentNavController().navigate(R.id.event_detail_fragment, args)
    }

}