package com.pavelhabzansky.citizenapp.features.news.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pavelhabzansky.citizenapp.features.news.view.CitizenNewsFragment
import com.pavelhabzansky.citizenapp.features.news.view.TouristNewsFragment

class NewsPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    companion object {
        const val CITIZEN_NEWS_POSITION = 0
        const val TOURIST_NEWS_POSITION = 1

        const val SCREEN_OFFSCREEN_LIMIT = 1

        const val ITEM_COUNT = 2
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            CITIZEN_NEWS_POSITION -> CitizenNewsFragment()
            else -> TouristNewsFragment()
        }
    }

}