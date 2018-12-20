package com.remedyal.footballapp.view.activity

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.remedyal.footballapp.R.id.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlayerDetailActivityTest {

    private lateinit var playerDetailIdlingResourceList: List<IdlingResource>

    @Rule
    @JvmField
    var playerDetailRule = ActivityTestRule(PlayerDetailActivity::class.java, false, false)

    @Before
    fun setUp() {
        val i = Intent()
        i.putExtra("id", "34145937")
        playerDetailRule.launchActivity(i)

        playerDetailIdlingResourceList = playerDetailRule.activity.getIdlingResources()
        for (item in playerDetailIdlingResourceList) {
            IdlingRegistry.getInstance().register(item)
        }
    }

    @After
    fun tearDown() {
        for (item in playerDetailIdlingResourceList) {
            IdlingRegistry.getInstance().unregister(item)
        }
    }

    @Test
    fun testCheckPlayerViewItems() {
        onView(withId(tv_player_description)).check(matches(isDisplayed()))
        onView(withId(tv_player_weight)).check(matches(isDisplayed()))
        onView(withId(tv_player_height)).check(matches(isDisplayed()))
        onView(withId(tv_player_position)).check(matches(isDisplayed()))
    }
}