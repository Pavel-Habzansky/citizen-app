package com.pavelhabzansky.citizenapp.features.news.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_KEY_NEWS_SOURCE
import com.pavelhabzansky.citizenapp.core.ARG_KEY_NEWS_TITLE
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fromJson
import com.pavelhabzansky.citizenapp.databinding.FragmentCitizenNewsBinding
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsSourceAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CitizenNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentCitizenNewsBinding

    private val viewModel by sharedViewModel<NewsViewModel>()

    private val newsSourceAdapter: NewsSourceAdapter by lazy {
        NewsSourceAdapter(
            onItemClick = this::onItemClick
        )
    }

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

        val recycler = binding.newsSourceRecycler

        recycler.setHasFixedSize(true)
        recycler.adapter = newsSourceAdapter

        registerEvents()

        val source = arguments?.getString(ARG_KEY_NEWS_SOURCE)
        binding.swipeContainer.setOnRefreshListener { viewModel.loadNews(force = true) }
        if (source != null) {
            val city = source.fromJson(CityInformationVO::class.java)
            viewModel.loadNewsForCity(city = city)
        } else {
            viewModel.loadCachedNews()
            viewModel.loadNews()
        }
    }

    private fun registerEvents() {
        viewModel.citizenNewsViewState.observe(this, Observer {
            updateViewState(event = it)
        })
        viewModel.newsErrorState.observe(this, Observer {
            Toast.makeText(context, "Došlo k chybě při stahování novinek", Toast.LENGTH_LONG).show()
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.NewsCacheLoadedViewState -> setNews(news = event.news)
            is NewsViewState.NewsLoadedViewState -> setNews(news = event.news)
        }
    }

    private fun setNews(news: List<NewsItemViewObject>) {
        newsSourceAdapter.setItems(newItems = news)
        binding.swipeContainer.isRefreshing = false

        if (news.isNotEmpty()) {
            binding.title.visibility = View.GONE
            binding.swipeContainer.visibility = View.VISIBLE
        }
    }

    private fun onItemClick(title: String, url: String) {
        if(arguments?.getString(ARG_KEY_NEWS_SOURCE).isNullOrEmpty()) {
            val args = Bundle().also { it.putString(ARG_KEY_NEWS_TITLE, title) }
            findNavController().navigate(R.id.news_detail_fragment, args)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

}