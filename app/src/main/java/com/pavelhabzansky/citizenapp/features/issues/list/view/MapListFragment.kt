package com.pavelhabzansky.citizenapp.features.issues.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.databinding.FragmentIssueListBinding
import com.pavelhabzansky.citizenapp.features.issues.list.states.MapListViewStates
import com.pavelhabzansky.citizenapp.features.issues.list.view.adapter.ItemListAdapter
import com.pavelhabzansky.citizenapp.features.issues.list.view.vm.MapListViewModel
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.MapItem
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.MapItemsVO
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MapListFragment : BaseFragment() {

    private lateinit var binding: FragmentIssueListBinding

    private val viewModel by viewModel<MapListViewModel>()

    private val adapter: ItemListAdapter by lazy {
        ItemListAdapter(onClick = this::onItemClick)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIssueListBinding.inflate(inflater, container, false)

        binding.issueRecycler.setHasFixedSize(true)
        binding.issueRecycler.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        binding.issueRecycler.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        arguments?.let {
            viewModel.useContext = it.getString(USE_CONTEXT_ARG, USE_CONTEXT_EMPTY)
        }

        viewModel.loadItems()
    }

    private fun registerEvents() {
        viewModel.mapListViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.mapListErrorState.observe(this, Observer {
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: MapListViewStates) {
        when (event) {
            is MapListViewStates.MapItemsLoaded -> updateItems(event.items)
        }
    }

    private fun updateItems(items: MapItemsVO) {
        binding.listRefresh.isRefreshing = false
        adapter.updateItems(newItems = items.mapItems)
    }

    private fun onItemClick(item: MapItem) {
        val itemJson = item.toJson()
        val args = Bundle()
        when (item) {
            is IssueVO -> {
                args.putString(ARG_ISSUE_DATA, itemJson)
                findParentNavController().navigate(R.id.issue_detail_fragment, args)
            }
            is PlaceVO -> {
                args.putString(ARG_PLACE_DATA, itemJson)
                findParentNavController().navigate(R.id.place_detail_fragment, args)
            }

        }
    }


}