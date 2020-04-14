package com.pavelhabzansky.citizenapp.features.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.databinding.FragmentFilterBinding
import com.pavelhabzansky.citizenapp.features.filter.states.FilterViewState
import com.pavelhabzansky.citizenapp.features.filter.view.adapter.CategoryFilterAdapter
import com.pavelhabzansky.citizenapp.features.filter.view.adapter.CityFilterAdapter
import com.pavelhabzansky.citizenapp.features.filter.view.vm.FilterViewModel
import com.pavelhabzansky.citizenapp.features.filter.view.vo.CitySettingVO
import org.koin.android.viewmodel.ext.android.viewModel

class FilterFragment : BaseFragment() {

    private lateinit var binding: FragmentFilterBinding

    private val viewModel by viewModel<FilterViewModel>()

    private val cityFilterAdapter: CityFilterAdapter by lazy {
        CityFilterAdapter(viewModel::onCitySettingCheck)
    }

//    private val categoryFilterAdapter: CategoryFilterAdapter by lazy {
//        CategoryFilterAdapter()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)

        binding.citiesFilterRecycler.setHasFixedSize(true)
        binding.citiesFilterRecycler.layoutManager = LinearLayoutManager(context)
        binding.citiesFilterRecycler.adapter = cityFilterAdapter

//        binding.categoriesFilterRecycler.setHasFixedSize(true)
//        binding.categoriesFilterRecycler.layoutManager = LinearLayoutManager(context)
//        binding.categoriesFilterRecycler.adapter = categoryFilterAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        viewModel.loadFilters()

        binding.allCheckCities.setOnClickListener { viewModel.setAll(binding.allCheckCities.isChecked) }
        binding.saveButton.setOnClickListener { viewModel.saveCurrent() }
    }

    private fun registerEvents() {
        viewModel.filterViewState.observe(this, Observer {
            updateViewState(it)
        })
    }

    private fun updateViewState(event: FilterViewState) {
        when (event) {
            is FilterViewState.CitySettingsLoadedEvent -> updateCityFilter(event.settings)
            is FilterViewState.FilterSavedEvent -> findParentNavController().navigateUp()
        }
    }

    private fun updateCityFilter(items: List<CitySettingVO>) {
        binding.allCheckCities.isChecked = items.all { it.enabled }
        cityFilterAdapter.updateSettings(items)
    }


}