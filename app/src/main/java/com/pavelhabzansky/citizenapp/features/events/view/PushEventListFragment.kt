package com.pavelhabzansky.citizenapp.features.events.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.hide
import com.pavelhabzansky.citizenapp.core.show
import com.pavelhabzansky.citizenapp.databinding.FragmentPushListBinding
import com.pavelhabzansky.citizenapp.features.events.states.PushViewStates
import com.pavelhabzansky.citizenapp.features.events.view.adapter.PushEventListAdapter
import com.pavelhabzansky.citizenapp.features.events.view.vm.PushEventListViewModel
import com.pavelhabzansky.citizenapp.features.events.view.vo.PushEventVO
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PushEventListFragment : BaseFragment() {

    private lateinit var binding: FragmentPushListBinding

    private val viewModel by viewModel<PushEventListViewModel>()

    private val adapter: PushEventListAdapter by lazy {
        PushEventListAdapter(onRemoveClick = this::removePushEvent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_push_list, container, false)
        registerEvents()

        binding.pushEventRecycler.setHasFixedSize(true)
        binding.pushEventRecycler.layoutManager = LinearLayoutManager(context)
        binding.pushEventRecycler.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadInbox()
    }

    private fun registerEvents() {
        viewModel.pushViewStates.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.pushErrorStates.observe(this, Observer {
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: PushViewStates) {
        when (event) {
            is PushViewStates.PushEventsLoaded -> updateItems(event.events)
        }
    }

    private fun removePushEvent(id: String) {
        viewModel.removeEvent(id)
    }

    private fun updateItems(items: List<PushEventVO>) {
        adapter.updateItems(items)

        if (items.isNotEmpty()) {
            binding.pushEventRecycler.show()
            binding.emptyTitle.hide()
        } else {
            binding.pushEventRecycler.hide()
            binding.emptyTitle.show()
        }
    }

}