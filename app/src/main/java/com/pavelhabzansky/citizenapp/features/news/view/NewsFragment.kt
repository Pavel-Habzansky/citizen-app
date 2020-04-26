package com.pavelhabzansky.citizenapp.features.news.view

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.FINE_LOCATION_REQ_NEWS
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.databinding.FragmentNewsBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsPagerAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.net.UnknownHostException

class NewsFragment : BaseFragment() {

    private val newsPagerAdapter: NewsPagerAdapter by lazy {
        NewsPagerAdapter(childFragmentManager, requireContext())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.locationRefresh -> {
                viewModel.requestLocationPermission()
                true
            }
            R.id.filter -> {
                findParentNavController().navigate(R.id.to_filter)
                true
            }
            else -> activity?.onOptionsItemSelected(item) ?: false
        }
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
            when (it.t) {
                is UnknownHostException -> toast("Nedostupné připojení")
            }
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
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