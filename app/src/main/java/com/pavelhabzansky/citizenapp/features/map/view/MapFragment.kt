package com.pavelhabzansky.citizenapp.features.map.view

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.databinding.FragmentMapBinding
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private val viewModel by sharedViewModel<MapViewModel>()

    private val locationClient by lazy {
        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val markers = mutableListOf<Pair<Marker, IssueVO>>()

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
        binding.mapSettingsFab.setOnClickListener { }
        binding.newIssueFab.setOnClickListener { createNewIssue() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        viewModel.fetchIssues()
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
            .setDuration(FAB_ANIMATE_DURATION)
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
            .setDuration(FAB_ANIMATE_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.VISIBLE
                }
            })
    }

    private fun animateRotateOpen(view: View) {
        view.animate()
            .rotation(45f)
            .setDuration(FAB_ANIMATE_DURATION)
    }

    private fun animateRotateClose(view: View) {
        view.animate()
            .rotation(0f)
            .setDuration(FAB_ANIMATE_DURATION)
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
            is MapViewStates.IssuesUpdatedEvent -> updateMarkers(issues = event.issues)
        }
    }

    private fun updateMarkers(issues: List<IssueVO>) {
        val currentIssues = markers.map { it.second }
        val newIssues = issues.minus(currentIssues)

        newIssues.forEach {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.lng))
                    .title(it.title)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            bitmapFromVector(requireContext(), it.type.icon)
                        )
                    )
            )
            markers.add(marker to it)
        }

        val oldIssues = currentIssues.minus(issues)
        val oldMarkers = markers.filter { oldIssues.contains(it.second) }
        oldMarkers.onEach { it.first.remove() }
        markers.removeAll(oldMarkers)
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        val data = markers.find { it.first == marker }?.second

        data?.let {
            val issueJson = it.toJson()
            val args = Bundle().also { it.putString(ARG_ISSUE_DATA, issueJson) }
            findParentNavController().navigate(R.id.issue_detail_fragment, args)

            return true
        }

        return false
    }

    private fun bitmapFromVector(context: Context, id: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, id)

        val bitmap = Bitmap.createBitmap(
            drawable?.intrinsicWidth ?: 0,
            drawable?.intrinsicHeight ?: 0,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)

        return bitmap
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            Timber.i("Map is obtained")

            googleMap = it
            googleMap.setOnMapLongClickListener { onMapLongClick(it) }
            googleMap.setOnMarkerClickListener { onMarkerClick(it) }

            googleMap.setOnCameraMoveListener {
                val bounds = getCurrentBounds()
                viewModel.loadIssues(bounds)
            }

            arguments?.let { args ->
                val latitude = args.getDouble(ARG_CITY_LAT)
                val longitude = args.getDouble(ARG_CITY_LNG)

                navigateToLocation(lat = latitude, lng = longitude)
            } ?: run {
                targetUser()
            }
        } ?: run {
            Timber.w("Couldn't obtain map - GoogleMap is null")
        }
    }

    private fun createNewIssue() {
        if (locationProvided()) {
            toggleFabMenu()

            val location = locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location == null) {
                Toast.makeText(context, "GPS lokace není dostupná", Toast.LENGTH_LONG).show()
                return
            }

            val args = Bundle()
            args.putDouble(ARG_KEY_LAT, location.latitude)
            args.putDouble(ARG_KEY_LNG, location.longitude)
            findParentNavController().navigate(R.id.issue_create_fragment, args)
        } else {
            Toast.makeText(context, "Lokace uživatele není povolena", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCurrentBounds(): Bounds {
        val west = googleMap.projection.visibleRegion.latLngBounds.southwest.longitude
        val north = googleMap.projection.visibleRegion.latLngBounds.northeast.latitude
        val east = googleMap.projection.visibleRegion.latLngBounds.northeast.longitude
        val south = googleMap.projection.visibleRegion.latLngBounds.southwest.latitude

        return Bounds(west, north, east, south)
    }

    private fun targetUser() {
        if (locationProvided()) {
            val location = locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                navigateToObject(lat = it.latitude, lng = it.longitude)
            }
        }
    }

    private fun locationProvided(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
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

    private fun onMapLongClick(position: LatLng) {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATE_LENGTH,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(VIBRATE_LENGTH)
        }

        if (fabOpen) {
            toggleFabMenu()
        }

        val args = Bundle()
        args.putDouble(ARG_KEY_LAT, position.latitude)
        args.putDouble(ARG_KEY_LNG, position.longitude)
        findParentNavController().navigate(R.id.to_create_issue, args)

    }

    companion object {

        const val CITY_ZOOM_LEVEL = 10f
        const val OBJECT_ZOOM_LEVEL = 15f

    }

}