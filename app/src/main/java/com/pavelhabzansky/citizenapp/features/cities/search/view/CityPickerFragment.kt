package com.pavelhabzansky.citizenapp.features.cities.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_CITY_KEY
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.core.fragment.hideKeyboard
import com.pavelhabzansky.citizenapp.databinding.FragmentCityPickBinding
import com.pavelhabzansky.citizenapp.features.cities.search.states.CityPickerViewStates
import com.pavelhabzansky.citizenapp.features.cities.search.view.adapter.CityAutocompleteArrayAdapter
import com.pavelhabzansky.citizenapp.features.cities.search.view.adapter.LastSearchAdapter
import com.pavelhabzansky.citizenapp.features.cities.search.view.vm.CityPickerViewModel
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.AutoCompleteItem
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityPickerFragment : BaseFragment() {

    private lateinit var binding: FragmentCityPickBinding

    private val viewModel by viewModel<CityPickerViewModel>()

    private val lastSearchAdapter: LastSearchAdapter by lazy {
        LastSearchAdapter(
            onClick = { item -> goToCityDetail(cityKey = item.key) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_pick, container, false)

        setupAutocomplete()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        setupRecycler()

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

    private fun updateAutoComplete(items: List<AutoCompleteItem>) {
        val cityPick = binding.cityPick
        cityPick.apply {
            setAdapter(
                CityAutocompleteArrayAdapter(context!!).also { it.addAll(items) }
            )
            if (cityPick.hasFocus()) showDropDown()
        }
    }

    private fun updateViewState(event: CityPickerViewStates) {
        when (event) {
            is CityPickerViewStates.LastUsedItemsLoadedEvent -> {
                lastSearchAdapter.updateItems(newItems = event.items)
            }
            is CityPickerViewStates.AutoCompleteLoadedEvent -> {
                updateAutoComplete(items = event.items)
            }
        }
    }

    private fun setupRecycler() {
        val recycler = binding.lastSearchesRecycler

        recycler.apply {
            setHasFixedSize(true)
            adapter = lastSearchAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }
    }

    private fun goToCityDetail(cityKey: String) {
        val args = Bundle()
        args.putString(ARG_CITY_KEY, cityKey)
        findParentNavController().navigate(R.id.city_detail_fragment, args)
        context?.let { hideKeyboard(it, binding.root) }
    }

    private fun setupAutocomplete() {
        val cityInput = binding.cityPick

        cityInput.apply {
            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDropDown() }
            doOnTextChanged { text, _, _, _ -> viewModel.onCityPickerTextUpdate(newText = text.toString()) }
            setOnItemClickListener { _, _, position, _ ->
                val adapter = adapter as CityAutocompleteArrayAdapter
                val selected = adapter.getItem(position)
                Timber.i("Selected Item: ${selected.toString()}")
                selected?.let {
                    viewModel.itemSelected(item = it)
                    goToCityDetail(cityKey = selected.key)
                }
            }
        }

    }

}