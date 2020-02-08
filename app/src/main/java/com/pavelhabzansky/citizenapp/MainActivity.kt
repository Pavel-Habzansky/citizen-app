package com.pavelhabzansky.citizenapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.pavelhabzansky.citizenapp.core.FINE_LOCATION_REQ
import com.pavelhabzansky.citizenapp.core.activity.BaseActivity
import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var appBarConfig: AppBarConfiguration

    private val mapViewModel by viewModel<MapViewModel>()

    private val navController: NavController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appBarConfig = AppBarConfiguration(navController.graph, drawer)

        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
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
            FINE_LOCATION_REQ -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Fine location permission granted")
                    mapViewModel.requestLocationPermission()
                }
            }
        }
    }
}
