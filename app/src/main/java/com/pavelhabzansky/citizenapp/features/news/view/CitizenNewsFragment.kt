package com.pavelhabzansky.citizenapp.features.news.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.databinding.FragmentCitizenNewsBinding
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsPagerAdapter

class CitizenNewsFragment : Fragment() {

    private lateinit var binding: FragmentCitizenNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_citizen_news, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}