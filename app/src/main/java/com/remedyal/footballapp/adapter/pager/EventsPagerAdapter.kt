package com.remedyal.footballapp.adapter.pager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.remedyal.footballapp.R.string.*
import com.remedyal.footballapp.view.fragment.child.NextEventsFragment
import com.remedyal.footballapp.view.fragment.child.PastEventsFragment

class EventsPagerAdapter(fm: FragmentManager, private val context: Context?) : FragmentPagerAdapter(fm) {

    private val pages : List<Fragment> = listOf(
        NextEventsFragment(),
        PastEventsFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.getString(tab_next)
            else -> context?.getString(tab_past)
        }
    }
}