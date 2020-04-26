package com.pavelhabzansky.citizenapp.features.news.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.features.news.view.CitizenNewsFragment
import com.pavelhabzansky.citizenapp.features.news.view.TouristNewsFragment

class NewsPagerAdapter(
        fragmentManager: FragmentManager,
        private val context: Context
) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        const val CITIZEN_NEWS_POSITION = 0
        const val TOURIST_NEWS_POSITION = 1

        const val SCREEN_OFFSCREEN_LIMIT = 1

        const val ITEM_COUNT = 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            CITIZEN_NEWS_POSITION -> context.getString(R.string.pager_title_citizen)
            else -> context.getString(R.string.pager_title_tourist)
        }
    }

    private val fragments = listOf(
            CitizenNewsFragment(),
            TouristNewsFragment()

    )

    override fun getCount(): Int {
        return ITEM_COUNT
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}