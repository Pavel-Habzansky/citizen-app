package com.pavelhabzansky.citizenapp.core.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pavelhabzansky.citizenapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseFragment : Fragment(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main + SupervisorJob()

    override fun onDestroy() {
        super.onDestroy()

        coroutineContext.cancel()
    }

}

fun Fragment.findParentNavController() = Navigation.findNavController(activity!!, R.id.navHostFragment)