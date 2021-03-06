package com.pavelhabzansky.citizenapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.pavelhabzansky.citizenapp.core.CAMERA_PERMISSION_REQ
import com.pavelhabzansky.citizenapp.core.FINE_LOCATION_REQ_MAP
import com.pavelhabzansky.citizenapp.core.FINE_LOCATION_REQ_NEWS
import com.pavelhabzansky.citizenapp.core.activity.BaseActivity
import com.pavelhabzansky.citizenapp.core.activity.hasLocationPermission
import com.pavelhabzansky.citizenapp.features.filter.view.FilterFragment
import com.pavelhabzansky.citizenapp.features.issues.create.view.vm.CreateIssueViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.domain.features.events.usecase.GetInboxSizeUseCase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var appBarConfig: AppBarConfiguration

    private val mapViewModel by viewModel<MapViewModel>()
    private val newsViewModel by viewModel<NewsViewModel>()
    private val createIssueViewModel by viewModel<CreateIssueViewModel>()

    private val getInboxSize by inject<GetInboxSizeUseCase>()
    private var inboxSize: LiveData<Int>? = null
    private val inboxObserver: Observer<Int> by lazy {
        Observer<Int> { updateInboxCounter(it) }
    }

    private val navController: NavController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appBarConfig = AppBarConfiguration(navController.graph, drawer)

        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
        subscribeInbox()
    }

    private fun subscribeInbox() {
        launch(Dispatchers.IO) {
            inboxSize = getInboxSize(Unit)
            launch(Dispatchers.Main) { inboxSize?.observeForever(inboxObserver) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        inboxSize?.removeObserver(inboxObserver)
    }

    private fun updateInboxCounter(size: Int) {
        val inboxSizeText = navView.menu.findItem(R.id.push_list_fragment).actionView.findViewById<TextView>(R.id.inboxCounter)
        inboxSizeText.text = if (size == 0) "" else size.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                findNavController(R.id.navHostFragment).navigate(R.id.to_settings_default)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val current = navHost?.childFragmentManager?.fragments?.get(0)
        if (current is FilterFragment) {
            menu?.findItem(R.id.filter)?.isVisible = false
        }

        setLocationIcon(menu?.findItem(R.id.locationRefresh))

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (!navController.navigateUp()) {
                super.onBackPressed()
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            FINE_LOCATION_REQ_MAP -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Fine location permission granted")
                    mapViewModel.requestLocationPermission()
                }
            }
            FINE_LOCATION_REQ_NEWS -> {
                if (grantResults.isNotEmpty()) {
                    invalidateOptionsMenu()
                    when (grantResults[0]) {
                        PackageManager.PERMISSION_GRANTED -> newsViewModel.requestLocationPermission()
                        else -> newsViewModel.locationPermissionDenied()
                    }
                }
            }
            CAMERA_PERMISSION_REQ -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Camera permission granted")
                    createIssueViewModel.requestCameraPermission()
                }
            }
        }
    }

    private fun setLocationIcon(item: MenuItem?) {
        when (hasLocationPermission()) {
            true -> item?.isVisible = false
            false -> item?.isVisible = true
        }
    }

}
