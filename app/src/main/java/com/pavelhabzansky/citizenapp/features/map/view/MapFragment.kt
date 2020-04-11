package com.pavelhabzansky.citizenapp.features.map.view

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.databinding.FragmentMapBinding
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO
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
    private lateinit var clusterManager: ClusterManager<ClusterItem>
    private lateinit var binding: FragmentMapBinding

    private var fabOpen = false

    private var loaded = false

    private lateinit var lastShownPosition: CameraPosition

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

        if (isCitizenContext() || isEmptyContext()) {
            binding.newIssueFab.setOnClickListener { createNewIssue() }
        }
        binding.toListFab.setOnClickListener { toMapList() }
        binding.mapSettingsFab.setOnClickListener { toSettings() }
        binding.mapTypeSwitch.setOnClickListener { switchMap() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
        registerReceiver()

        arguments?.let {
            val context = it.getString(USE_CONTEXT_ARG, USE_CONTEXT_EMPTY)
            viewModel.useContext = context
            viewModel.loadData()
        }

        viewModel.requestLocationPermission()
    }

    private fun reloadData() {
        viewModel.requestLocationPermission()
        viewModel.loadData()
    }

    private fun registerReceiver() {
        val receiver = connectivityManager.createNetworkReceiver {
            if (it) {
                reloadData()
                binding.mapContainer.show()
                binding.disconnectedTitle.hide()
            } else {
                binding.mapContainer.hide()
                binding.disconnectedTitle.show()
            }
        }
        context?.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun switchMap() {
        if (this::googleMap.isInitialized) {
            when (googleMap.mapType) {
                GoogleMap.MAP_TYPE_SATELLITE -> googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                GoogleMap.MAP_TYPE_NORMAL -> googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                GoogleMap.MAP_TYPE_TERRAIN -> googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
        }
    }

    private fun toggleFabMenu() {
        if (fabOpen) {
            animateRotateClose(binding.mainFab)
            animateFade(binding.mapSettingsFab)
            if (isCitizenContext() || isEmptyContext()) {
                animateFade(binding.newIssueFab)
                animateFade(binding.toListFab)
            }
        } else {
            animateRotateOpen(binding.mainFab)
            animateShow(binding.mapSettingsFab)
            if (isCitizenContext() || isEmptyContext()) {
                animateShow(binding.newIssueFab)
                animateShow(binding.toListFab)
            }
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
            is MapViewStates.PlacesNoConnectionEvent -> toast("Nedostupné připojení - Nelze najít místa")
            is MapViewStates.NoContextProvided -> toast("Nebyl poskytnut kontext")
            is MapViewStates.LocationPermissionGranted -> {
                if (this::googleMap.isInitialized) {
                    googleMap.isMyLocationEnabled = true
                    val location = locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    targetUser()
                    viewModel.fetchPlaces(location.latitude, location.longitude)
                }
            }
            is MapViewStates.LocationPermissionNotGranted -> {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_REQ_MAP
                )
            }
            is MapViewStates.PlacesLoadedEvent -> {
                if (this::clusterManager.isInitialized) {
                    clusterManager.addItems(event.places)
                    clusterManager.cluster()
                }
            }
            is MapViewStates.IssuesUpdatedEvent -> {
                // TODO Change getting current markers
                val currentIssues = markers.map { it.second }
                val newIssues = event.issues.minus(currentIssues)

                if (this::clusterManager.isInitialized) {
                    clusterManager.removeItems(currentIssues)
                    clusterManager.addItems(newIssues)
                    clusterManager.cluster()
                }
            }
        }
    }

    private fun toSettings() {
        findParentNavController().navigate(R.id.to_settings)
    }

    private fun onIssueClick(issue: IssueVO) {
        val issueJson = issue.toJson()
        val args = Bundle().also { it.putString(ARG_ISSUE_DATA, issueJson) }
        findParentNavController().navigate(R.id.issue_detail_fragment, args)
    }

    private fun onPlaceClick(place: PlaceVO) {
        val placeJson = place.toJson()
        val args = Bundle().also { it.putString(ARG_PLACE_DATA, placeJson) }
        findParentNavController().navigate(R.id.place_detail_fragment, args)
    }

    private fun onClusterItemClick(item: ClusterItem): Boolean {
        when (item) {
            is IssueVO -> onIssueClick(item)
            is PlaceVO -> onPlaceClick(item)
        }

        return true
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it

            clusterManager = ClusterManager(context, googleMap)
            clusterManager.renderer = PlaceClusterRenderer(requireContext(), googleMap, clusterManager)
            clusterManager.setOnClusterItemClickListener { onClusterItemClick(it) }

            googleMap.setOnCameraIdleListener(clusterManager)
            googleMap.setOnMapLongClickListener { onMapLongClick(it) }
            googleMap.setOnMarkerClickListener(clusterManager)
            googleMap.setOnCameraMoveListener {
                lastShownPosition = it.cameraPosition
                loadIssueInBounds()
            }

            arguments?.let { args ->
                val latitude = args.getDouble(ARG_CITY_LAT)
                val longitude = args.getDouble(ARG_CITY_LNG)

                navigateToLocation(lat = latitude, lng = longitude)
            } ?: run {
                if (!loaded) {
                    loaded = true
                    targetUser()
                } else {
                    if (!this::lastShownPosition.isInitialized) {
                        targetUser()
                    } else {
                        it.moveCamera(CameraUpdateFactory.newCameraPosition(lastShownPosition))
                    }
                }
            }

            viewModel.loadPlaces()
            loadIssueInBounds()
        } ?: run {
            Timber.w("Couldn't obtain map - GoogleMap is null")
        }
    }

    private fun loadIssueInBounds() {
        val bounds = getCurrentBounds()
        viewModel.loadIssues(bounds)
    }

    private fun createNewIssue() {
        if (locationProvided()) {
            toggleFabMenu()

            val location = locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location == null) {
                toast("GPS lokace není dostupná")
                return
            }

            val args = Bundle()
            args.putDouble(ARG_KEY_LAT, location.latitude)
            args.putDouble(ARG_KEY_LNG, location.longitude)
            findParentNavController().navigate(R.id.issue_create_fragment, args)
        } else {
            toast("Lokace uživatele není povolena")
        }
    }

    private fun toMapList() {
        toggleFabMenu()
        findParentNavController().navigate(R.id.to_map_list)
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
            location?.let { navigateToObject(lat = it.latitude, lng = it.longitude) }
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
        if ((isCitizenContext() || isEmptyContext()) && connectivityManager.isConnected()) {
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
    }

    private fun isEmptyContext(): Boolean = viewModel.useContext == USE_CONTEXT_EMPTY

    private fun isCitizenContext(): Boolean = viewModel.useContext == USE_CONTEXT_CITIZEN

    private fun isTouristContext(): Boolean = viewModel.useContext == USE_CONTEXT_TOURIST

    companion object {

        const val CITY_ZOOM_LEVEL = 10f
        const val OBJECT_ZOOM_LEVEL = 15f

    }

}