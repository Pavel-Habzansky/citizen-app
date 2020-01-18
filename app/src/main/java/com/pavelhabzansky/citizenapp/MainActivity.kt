package com.pavelhabzansky.citizenapp

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pavelhabzansky.citizenapp.core.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("Startup")

        val navController = findNavController(R.id.navHostFragment)
        appBarConfig = AppBarConfiguration(navController.graph, drawer)
//        collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfig)
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}
