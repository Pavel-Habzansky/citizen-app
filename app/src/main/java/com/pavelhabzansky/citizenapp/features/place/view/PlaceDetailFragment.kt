package com.pavelhabzansky.citizenapp.features.place.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_PLACE_DATA
import com.pavelhabzansky.citizenapp.core.WAZE_ID
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.core.fromJson
import com.pavelhabzansky.citizenapp.databinding.FragmentPlaceDetailBinding
import com.pavelhabzansky.citizenapp.features.place.states.PlaceDetailViewStates
import com.pavelhabzansky.citizenapp.features.place.view.adapter.GalleryAdapter
import com.pavelhabzansky.citizenapp.features.place.view.vm.PlaceDetailViewModel
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PlaceDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentPlaceDetailBinding

    private val viewModel by viewModel<PlaceDetailViewModel>()

    private val adapter: GalleryAdapter by lazy {
        GalleryAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_detail, container, false)

        binding.gallery.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@PlaceDetailFragment.adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        val placeJson = arguments?.getString(ARG_PLACE_DATA)
        placeJson?.let {
            val place = it.fromJson(PlaceVO::class.java)
            binding.setVariable(BR.place, place)
            binding.executePendingBindings()

            when (place.open) {
                true -> {
                    binding.openedText.text = getString(R.string.place_detail_open)
                    binding.openedText.setTextColor(resources.getColor(R.color.green))
                }
                false -> {
                    binding.openedText.text = getString(R.string.place_detail_closed)
                    binding.openedText.setTextColor(resources.getColor(R.color.red))
                }
                else -> binding.openedText.visibility = View.GONE
            }

            viewModel.loadPlaceImage(placeId = place.placeId)

            binding.navigateButton.setOnClickListener { navigate() }
        } ?: run {
            toast("Nevalidní data pro vybrané místo")
        }
    }

    private fun navigate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("geo:" + binding.place?.lat + "," + binding.place?.lng)
        startActivity(intent)
    }

    private fun isPackageInstalled(name: String): Boolean {
        val pm = context?.packageManager
        try {
            pm?.getPackageInfo(name, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    private fun registerEvents() {
        viewModel.placeDetailViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.placeDetailErrorState.observe(this, Observer {
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: PlaceDetailViewStates) {
        when (event) {
            is PlaceDetailViewStates.GalleryLoadedState -> adapter.setItems(event.gallery)
        }
    }

}