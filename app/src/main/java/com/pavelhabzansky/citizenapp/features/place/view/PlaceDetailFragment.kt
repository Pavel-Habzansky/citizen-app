package com.pavelhabzansky.citizenapp.features.place.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_PLACE_DATA
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fromJson
import com.pavelhabzansky.citizenapp.databinding.FragmentPlaceDetailBinding
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO

class PlaceDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentPlaceDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeJson = arguments?.getString(ARG_PLACE_DATA)
        placeJson?.let {
            val place = it.fromJson(PlaceVO::class.java)
            binding.setVariable(BR.place, place)
            binding.executePendingBindings()
        } ?: run {
            Toast.makeText(context, "Nevalidní data pro vybrané místo", Toast.LENGTH_LONG).show()
        }
    }

}