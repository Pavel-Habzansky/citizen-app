package com.pavelhabzansky.citizenapp.features.events.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rjeschke.txtmark.Processor
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_EVENT_DATA
import com.pavelhabzansky.citizenapp.core.ARG_EVENT_IMAGE
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fromJson
import com.pavelhabzansky.citizenapp.core.show
import com.pavelhabzansky.citizenapp.databinding.FragmentEventDetailBinding
import com.pavelhabzansky.citizenapp.features.events.states.EventsViewStates
import com.pavelhabzansky.citizenapp.features.events.view.adapter.EventsGalleryAdapter
import com.pavelhabzansky.citizenapp.features.events.view.vm.EventDetailViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vo.ScheduleViewObject
import io.noties.markwon.Markwon
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class EventDetailFragment : BaseFragment() {

    private val viewModel by viewModel<EventDetailViewModel>()

    private lateinit var binding: FragmentEventDetailBinding

    private val galleryAdapter: EventsGalleryAdapter by lazy {
        EventsGalleryAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false)

        binding.eventGallery.setHasFixedSize(true)
        binding.eventGallery.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.eventGallery.adapter = galleryAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        arguments?.let {
            val json = it.getString(ARG_EVENT_DATA)
            val imgData = it.getByteArray(ARG_EVENT_IMAGE)

            val schedule = json?.fromJson(ScheduleViewObject::class.java)

            if (schedule != null) {
                binding.setVariable(BR.item, schedule)
                binding.executePendingBindings()

                val markwon = Markwon.create(binding.root.context)
                markwon.setMarkdown(binding.eventText, schedule.text)

                if (imgData != null) {
                    val bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.size)
                    schedule.bitmap = bitmap
                    binding.eventMainImage.setImageBitmap(schedule.bitmap)
                }

                binding.detailLink.show()
                binding.detailLink.setOnClickListener { openDetail(schedule.url) }

                viewModel.loadGallery(schedule.images)
            }
        }
    }

    private fun registerEvents() {
        viewModel.eventDetailViewState.observe(this, Observer {
            updateViewState(it)
        })
        viewModel.errorViewState.observe(this, Observer {
            Timber.w(it.t, "Exception occured")
        })
    }

    private fun updateViewState(event: EventsViewStates) {
        when (event) {
            is EventsViewStates.GalleryDownloaded -> galleryAdapter.updateGallery(event.gallery)
        }
    }

    private fun openDetail(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }


}