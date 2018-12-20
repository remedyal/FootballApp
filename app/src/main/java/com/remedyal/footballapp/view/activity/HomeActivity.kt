package com.remedyal.footballapp.view.activity

import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import com.remedyal.footballapp.R
import com.remedyal.footballapp.R.id.*
import com.remedyal.footballapp.R.layout.activity_home
import com.remedyal.footballapp.view.fragment.parent.EventsParentFragment
import com.remedyal.footballapp.view.fragment.parent.FavoritesParentFragment
import com.remedyal.footballapp.view.fragment.parent.TeamsParentFragment


class HomeActivity : AppCompatActivity() {

    val getEventsIdlingResource = CountingIdlingResource("Get_Events")
    val getTeamsIdlingResource = CountingIdlingResource("Get_Teams")

    fun getIdlingResources(): List<CountingIdlingResource> {
        return listOf(getEventsIdlingResource, getTeamsIdlingResource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_home)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                mi_matches -> loadHomeFragment(savedInstanceState, EventsParentFragment())
                mi_teams -> loadHomeFragment(savedInstanceState, TeamsParentFragment())
                mi_favorites -> loadHomeFragment(savedInstanceState, FavoritesParentFragment())
            }
            true
        }

        bottom_navigation.selectedItemId = mi_matches
    }

    private fun loadHomeFragment(savedInstanceState: Bundle?, eventFragment: Fragment) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_child_container, eventFragment, eventFragment::class.java.simpleName)
                .commit()
        }
    }
}