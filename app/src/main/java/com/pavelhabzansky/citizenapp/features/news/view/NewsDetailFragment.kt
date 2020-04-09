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
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_KEY_NEWS_TITLE
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.core.toFormattedString
import com.pavelhabzansky.citizenapp.databinding.FragmentNewsDetailBinding
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class NewsDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsDetailBinding

    private val viewModel by viewModel<NewsDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARG_KEY_NEWS_TITLE)
        title?.let { viewModel.loadItem(title = it) }

        registerEvents()
    }

    private fun registerEvents() {
        viewModel.newsDetailViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.newsErrorViewState.observe(this, Observer {
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: NewsViewState) {
        when (event) {
            is NewsViewState.NewsItemLoadedViewState -> {
                binding.setVariable(BR.item, event.item)
                binding.executePendingBindings()
                binding.date.text = event.item.date?.toFormattedString() ?: ""
                binding.newsLink.setOnClickListener { onLinkClick(url = event.item.url) }
            }
        }
    }

    private fun onLinkClick(url: String?) {
        if (url == null) {
            toast(R.string.news_link_invalid)
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}