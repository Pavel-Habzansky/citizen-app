package com.pavelhabzansky.citizenapp.features.news.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.FragmentTouristNewsBinding

class TouristNewsFragment : Fragment() {

    private lateinit var binding: FragmentTouristNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tourist_news, container, false)

        return binding.root
    }

}