package com.pavelhabzansky.citizenapp.features.cities.detail.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_CITY_KEY
import com.pavelhabzansky.citizenapp.core.ARG_CITY_LAT
import com.pavelhabzansky.citizenapp.core.ARG_CITY_LNG
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentCityDetailBinding
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailViewStates
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vm.CityDetailViewModel
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
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

        binding.showOnMapButton.setOnClickListener { showOnMap() }
        binding.cityWebPageButton.setOnClickListener { showWeb() }
    }

    private fun updateViewState(state: CityDetailViewStates) {
        when (state) {
            is CityDetailViewStates.CityInformationLoaded -> setCityData(city = state.info)
        }
    }

    private fun setCityData(city: CityInformationVO) {
        binding.setVariable(BR.info, city)
        binding.executePendingBindings()

        val bytes = city.logoBytes
        bytes?.let {
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.cityLogo.setImageBitmap(bmp)
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

    private fun showOnMap() {
        val lat = viewModel.cityInfo.lat
        val lng = viewModel.cityInfo.lng

        val args = Bundle()

        if (lat != null && lng != null) {
            args.putDouble(ARG_CITY_LAT, lat)
            args.putDouble(ARG_CITY_LNG, lng)
        }

        findNavController().navigate(R.id.map_fragment, args)
    }

    private fun showWeb() {
        val www = viewModel.cityInfo.www
        if (www.isNotEmpty()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(www))
            startActivity(browserIntent)
        } else {
            Toast.makeText(context, "Město nemá webové stránky", Toast.LENGTH_LONG).show()
        }

    }

}