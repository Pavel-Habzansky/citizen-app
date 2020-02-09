package com.pavelhabzansky.citizenapp.features.news.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentNewsBinding
import com.pavelhabzansky.citizenapp.features.news.view.adapter.NewsPagerAdapter
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment() {

    private val newsPagerAdapter: NewsPagerAdapter by lazy {
        NewsPagerAdapter(childFragmentManager)
    }

    private lateinit var binding: FragmentNewsBinding

    private val viewModel by sharedViewModel<NewsViewModel>()

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

    }

    private fun initViewPager() {
        val viewPager = binding.newsViewPager
        val tabLayout = binding.newsTabLayout

        viewPager.adapter = newsPagerAdapter
        viewPager.offscreenPageLimit = NewsPagerAdapter.SCREEN_OFFSCREEN_LIMIT as Int

        tabLayout.apply {
            setupWithViewPager(viewPager)
        }


    }

}