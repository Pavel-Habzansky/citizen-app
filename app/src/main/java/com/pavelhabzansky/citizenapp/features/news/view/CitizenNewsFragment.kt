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
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentCitizenNewsBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsSourceAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CitizenNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentCitizenNewsBinding

    private val viewModel by sharedViewModel<NewsViewModel>()

    private val newsSourceAdapter: NewsSourceAdapter by lazy {
        NewsSourceAdapter(
            onLinkClick = this::onLinkClick
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

        viewModel.loadCachedNews()
        viewModel.loadNews()
    }

    private fun registerEvents() {
        viewModel.newsViewState.observe(this, Observer {
            updateViewState(event = it)
        })
        viewModel.newsErrorState.observe(this, Observer {
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.NewsCacheLoadedViewState -> {
                newsSourceAdapter.setItems(newItems = event.news)

                if (event.news.isNotEmpty()) {
                    binding.title.visibility = View.GONE
                    binding.newsSourceRecycler.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onLinkClick(url: String?) {
        if (url == null) {
            Toast.makeText(context, R.string.news_link_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}