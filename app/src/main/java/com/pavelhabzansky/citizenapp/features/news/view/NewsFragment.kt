package com.pavelhabzansky.citizenapp.features.news.view

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.FINE_LOCATION_REQ_NEWS
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.hasConnection
import com.pavelhabzansky.citizenapp.databinding.FragmentNewsBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsPagerAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.net.UnknownHostException

class NewsFragment : BaseFragment() {

    private val newsPagerAdapter: NewsPagerAdapter by lazy {
        NewsPagerAdapter(childFragmentManager)
    }

    private lateinit var binding: FragmentNewsBinding

    private val viewModel by sharedViewModel<NewsViewModel>()

    private val locationClient by lazy {
        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (newsViewPager.adapter == null) {
            initViewPager()
        }

        registerEvents()
        viewModel.requestLocationPermission()
    }

    private fun initViewPager() {
        val viewPager = binding.newsViewPager
        val tabLayout = binding.newsTabLayout

        viewPager.adapter = newsPagerAdapter
        viewPager.offscreenPageLimit = NewsPagerAdapter.SCREEN_OFFSCREEN_LIMIT

        tabLayout.apply {
            setupWithViewPager(viewPager)
        }
    }

    private fun registerEvents() {
        viewModel.newsViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.newsErrorState.observe(this, Observer {
            when(it.t) {
                is UnknownHostException -> Toast.makeText(context, "Nedostupné připojení", Toast.LENGTH_LONG).show()
            }
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.LocationPermissionGranted -> {
                if(hasConnection()) {
                    val location = locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    location?.let { viewModel.loadTouristNews(it.latitude, it.longitude) }
                }
            }
            is NewsViewState.LocationPermissionNotGranted -> {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_REQ_NEWS
                )
            }
        }
    }

}