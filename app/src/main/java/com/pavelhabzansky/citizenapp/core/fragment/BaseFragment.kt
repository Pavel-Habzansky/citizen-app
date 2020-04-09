package com.pavelhabzansky.citizenapp.core.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.domain.features.connectivity.manager.IConnectivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment(), CoroutineScope {

    val connectivityManager: IConnectivityManager by inject()

    override val coroutineContext = Dispatchers.Main + SupervisorJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        coroutineContext.cancel()
    }

}

fun Fragment.findParentNavController() = Navigation.findNavController(activity!!, R.id.navHostFragment)

fun Fragment.hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.toast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(@StringRes textRes: Int) {
    Toast.makeText(context, getString(textRes), Toast.LENGTH_LONG).show()
}
