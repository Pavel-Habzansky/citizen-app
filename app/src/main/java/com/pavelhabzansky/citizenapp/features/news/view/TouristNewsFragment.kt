package com.pavelhabzansky.citizenapp.features.news.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_KEY_NEWS_TITLE
import com.pavelhabzansky.citizenapp.core.ARG_KEY_NEWS_URL
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.core.hide
import com.pavelhabzansky.citizenapp.core.show
import com.pavelhabzansky.citizenapp.databinding.FragmentTouristNewsBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsSourceAdapter
import com.pavelhabzansky.citizenapp.features.news.view.adapter.TouristNewsAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.net.UnknownHostException

class TouristNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentTouristNewsBinding

    private val locationClient by lazy {
        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val viewModel by sharedViewModel<NewsViewModel>()

    private val newsAdapter: NewsSourceAdapter by lazy {
        NewsSourceAdapter(onItemClick = this::onItemClick)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tourist_news, container, false)

        binding.newsSourceRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        binding.swipeContainer.setOnRefreshListener { loadTouristNews() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents() {
        viewModel.touristNewsViewState.observe(this, Observer {
            updateViewState(event = it)
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
            is NewsViewState.TouristNewsLoaded -> updateList(event.news)
            is NewsViewState.NoConnectionEvent -> noConnectionAvailable()
            is NewsViewState.LocationPermissionNotGranted -> noLocationAvailable()
        }
    }

    private fun loadTouristNews() {
        if(!viewModel.hasLocationPermission()) {
            binding.swipeContainer.isRefreshing = false
            toast(R.string.location_unavailable)
            return
        }

        val location = locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location == null) {
            binding.swipeContainer.isRefreshing = false
            toast(R.string.location_unavailable)
            return
        }

        viewModel.loadTouristNews(location.latitude, location.longitude)
    }

    private fun updateList(news: List<NewsItemViewObject>) {
        binding.swipeContainer.isRefreshing = false
        newsAdapter.setItems(news)

        when (news.isEmpty()) {
            true -> noNewsAvailable()
            else -> showNews()
        }
    }

    private fun noLocationAvailable() {
        binding.swipeContainer.isRefreshing = false
        binding.newsSourceRecycler.hide()
        binding.title.show()
        binding.title.text = getString(R.string.location_unavailable)
    }

    private fun noConnectionAvailable() {
        binding.swipeContainer.isRefreshing = false
        binding.newsSourceRecycler.hide()
        binding.title.show()
        binding.title.text = getString(R.string.no_connection)
    }

    private fun showNews() {
        binding.title.hide()
        binding.newsSourceRecycler.show()
    }

    private fun noNewsAvailable() {
        binding.title.text = getString(R.string.news_no_news_available)
        binding.title.show()
        binding.newsSourceRecycler.hide()
    }

    private fun onItemClick(title: String, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}