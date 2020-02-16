package com.pavelhabzansky.citizenapp.features.map.view

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_CITY_LAT
import com.pavelhabzansky.citizenapp.core.ARG_CITY_LNG
import com.pavelhabzansky.citizenapp.core.FINE_LOCATION_REQ
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentMapBinding
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private val viewModel by sharedViewModel<MapViewModel>()

    private val locationClient by lazy {
        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentMapBinding

    private var fabOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        val mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        binding.mainFab.setOnClickListener { toggleFabMenu() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        viewModel.requestLocationPermission()
    }

    private fun toggleFabMenu() {
        if (fabOpen) {
            animateRotateClose(binding.mainFab)
            animateFade(binding.mapSettingsFab)
            animateFade(binding.newIssueFab)
        } else {
            animateRotateOpen(binding.mainFab)
            animateShow(binding.mapSettingsFab)
            animateShow(binding.newIssueFab)
        }
        fabOpen = !fabOpen
    }

    private fun animateFade(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(150)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.INVISIBLE
                }
            })
    }

    private fun animateShow(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(150)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.VISIBLE
                }
            })
    }

    private fun animateRotateOpen(view: View) {
        view.animate()
            .rotation(45f)
            .setDuration(150)
    }

    private fun animateRotateClose(view: View) {
        view.animate()
            .rotation(0f)
            .setDuration(150)
    }

    private fun registerEvents() {
        viewModel.mapViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.mapErrorState.observe(this, Observer {
            Timber.e(it.t, "Unexpected error event on MapFragment")
        })
    }

    private fun updateViewState(event: MapViewStates) {
        when (event) {
            is MapViewStates.LocationPermissionGranted -> {
                googleMap.isMyLocationEnabled = true
            }
            is MapViewStates.LocationPermissionNotGranted -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_REQ
                )
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            Timber.i("Map is obtained")

            googleMap = it
            googleMap.setOnMapLongClickListener { onMapLongClick() }

            arguments?.let { args ->
                val latitude = args.getDouble(ARG_CITY_LAT)
                val longitude = args.getDouble(ARG_CITY_LNG)

                navigateToLocation(lat = latitude, lng = longitude)
            } ?: run {
                targetUser()
            }

            viewModel.fetchIssues()
        } ?: run {
            Timber.w("Couldn't obtain map - GoogleMap is null")
        }
    }

    private fun targetUser() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationClient.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                navigateToObject(lat = it.latitude, lng = it.longitude)
            }
        }
    }


    private fun navigateToLocation(lat: Double, lng: Double) {
        googleMap.let {
            val location = LatLng(lat, lng)
            val cameraPos = CameraPosition.Builder()
                .target(location)
                .zoom(CITY_ZOOM_LEVEL)
                .build()
            it.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))
        }
    }

    private fun navigateToObject(lat: Double, lng: Double) {
        googleMap.let {
            val location = LatLng(lat, lng)
            val cameraPos = CameraPosition.Builder()
                .target(location)
                .zoom(OBJECT_ZOOM_LEVEL)
                .build()
            it.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))
        }
    }

    private fun onMapLongClick() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(100)
        }


        Toast.makeText(context, "Long click on map", Toast.LENGTH_LONG).show()
    }

    companion object {

        const val CITY_ZOOM_LEVEL = 10f
        const val OBJECT_ZOOM_LEVEL = 15f

    }

}