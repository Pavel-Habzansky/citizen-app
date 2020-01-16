package com.pavelhabzansky.citizenapp.features.news.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentTouristNewsBinding
import com.pavelhabzansky.citizenapp.features.news.view.adapter.TouristNewsAdapter

class TouristNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentTouristNewsBinding

    private val touristNewsAdapter: TouristNewsAdapter by lazy {
        TouristNewsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tourist_news, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = binding.touristNewsRecycler

        recycler.setHasFixedSize(true)
        recycler.adapter = touristNewsAdapter
    }

}