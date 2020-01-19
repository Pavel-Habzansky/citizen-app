package com.pavelhabzansky.citizenapp.features.cities.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_CITY_KEY
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentCityDetailBinding
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailViewStates
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vm.CityDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentCityDetailBinding

    private val viewModel by viewModel<CityDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        val cityKey = arguments?.getString(ARG_CITY_KEY)
        cityKey?.let { viewModel.loadCityInfo(key = it) }
    }

    private fun updateViewState(state: CityDetailViewStates) {
        when (state) {
            is CityDetailViewStates.CityInformationLoaded -> {
                binding.setVariable(BR.info, state.info)
                binding.executePendingBindings()
            }
        }
    }

    private fun registerEvents() {
        viewModel.cityDetailViewState.observe(this, Observer {
            updateViewState(state = it)
        })

        viewModel.cityDetailErrorState.observe(this, Observer {
            Timber.e(it.error)
        })
    }

}