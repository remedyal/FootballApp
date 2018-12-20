package com.remedyal.footballapp.adapter.pager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.remedyal.footballapp.R.string.*
import com.remedyal.footballapp.view.fragment.child.OverviewFragment
import com.remedyal.footballapp.view.fragment.child.PlayersFragment

class TeamDetailPagerAdapter(fm: FragmentManager, private val context: Context?, teamDescription: String, teamId: String) : FragmentPagerAdapter(fm) {

    private val pages: List<Fragment> = listOf(
        OverviewFragment.newInstance(teamDescription),
        PlayersFragment.newInstance(teamId)
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.getString(tab_overview)
            else -> context?.getString(tab_players)
        }
    }
}