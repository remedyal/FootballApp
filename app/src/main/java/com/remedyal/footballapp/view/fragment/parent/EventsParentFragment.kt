package com.remedyal.footballapp.view.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.remedyal.footballapp.R
import kotlinx.android.synthetic.main.fragment_parent_tabbed.*
import com.remedyal.footballapp.R.layout.*
import com.remedyal.footballapp.adapter.pager.EventsPagerAdapter


class EventsParentFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(fragment_parent_tabbed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tb_parent.title = resources.getString(R.string.app_name)
        (activity as AppCompatActivity).setSupportActionBar(tb_parent)
        vp_main.adapter = EventsPagerAdapter(childFragmentManager, context)
        tl_main.setupWithViewPager(vp_main)
    }
}