package com.pavelhabzansky.citizenapp.core.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pavelhabzansky.citizenapp.core.LANG_CS
import com.pavelhabzansky.citizenapp.core.LANG_PREF_KEY
import com.pavelhabzansky.citizenapp.core.USER_PREF_SPACE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.util.*

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main + SupervisorJob()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context?): Context? {
        val prefs = context?.getSharedPreferences(USER_PREF_SPACE, Context.MODE_PRIVATE)
        prefs?.let {
            val langPref = it.getString(LANG_PREF_KEY, LANG_CS)

            val locale = Locale(langPref)
            Locale.setDefault(locale)

            val config = Configuration(context.resources?.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }

        return context?.createConfigurationContext(Configuration(context.resources?.configuration))
    }

    override fun onDestroy() {
        super.onDestroy()

        coroutineContext.cancel()
    }

}

fun AppCompatActivity.hasLocationPermission() =
        ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.toast(@StringRes textRes: Int) {
    Toast.makeText(this, getString(textRes), Toast.LENGTH_LONG).show()
}
