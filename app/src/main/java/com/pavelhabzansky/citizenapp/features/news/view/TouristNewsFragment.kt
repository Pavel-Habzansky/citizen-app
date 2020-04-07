package com.pavelhabzansky.citizenapp.features.news.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
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
            when(it.t) {
                is UnknownHostException -> Toast.makeText(context, "Nedostupné připojení", Toast.LENGTH_LONG).show()
            }
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.TouristNewsLoaded -> updateList(event.news)
        }
    }

    private fun updateList(news: List<NewsItemViewObject>) {
        binding.swipeContainer.isRefreshing = false
        newsAdapter.setItems(news)
    }

    private fun onItemClick(title: String, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}