package com.pavelhabzansky.citizenapp.features.news.view

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
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentTouristNewsBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsSourceAdapter
import com.pavelhabzansky.citizenapp.features.news.view.adapter.TouristNewsAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

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
            Toast.makeText(context, "Došlo k chybě při stahování novinek", Toast.LENGTH_LONG).show()
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.TouristNewsLoaded -> newsAdapter.setItems(event.news)
        }
    }

    private fun onItemClick(title: String) {
        val args = Bundle().also { it.putString(ARG_KEY_NEWS_TITLE, title) }
        findNavController().navigate(R.id.news_detail_fragment, args)
    }

}