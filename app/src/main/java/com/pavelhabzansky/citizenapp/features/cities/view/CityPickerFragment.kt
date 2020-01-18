package com.pavelhabzansky.citizenapp.features.cities.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentCityPickBinding
import com.pavelhabzansky.citizenapp.features.cities.states.CityPickerViewStates
import com.pavelhabzansky.citizenapp.features.cities.view.adapter.LastSearchAdapter
import com.pavelhabzansky.citizenapp.features.cities.view.vm.CityPickerViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityPickerFragment : BaseFragment() {

    private lateinit var binding: FragmentCityPickBinding

    private val viewModel by viewModel<CityPickerViewModel>()

    private val lastSearchAdapter: LastSearchAdapter by lazy {
        LastSearchAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_pick, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        setupRecycler()
        setupAutocomplete()

        viewModel.onCityPickerTextUpdate()

        viewModel.loadLastUsedSearches()
    }

    private fun registerEvents() {
        viewModel.viewStateEvent.observe(
            this,
            Observer {
                updateViewState(event = it)
            }
        )

        viewModel.errorViewState.observe(this, Observer {
            Timber.e(it.error)
        })
    }

    private fun updateViewState(event: CityPickerViewStates) {
        when (event) {
            is CityPickerViewStates.LastUsedItemsLoadedEvent -> {
                lastSearchAdapter.updateItems(newItems = event.items)
            }
        }
    }

    private fun setupRecycler() {
        val recycler = binding.lastSearchesRecycler

        recycler.apply {
            setHasFixedSize(true)
            adapter = lastSearchAdapter
        }
    }

    private fun setupAutocomplete() {
        val autoCompleteTextureView = binding.cityPick

        autoCompleteTextureView.doOnTextChanged { text, start, count, after ->
            //            viewModel.onCityPickerTextUpdate(newText = text.toString())
        }
    }

}