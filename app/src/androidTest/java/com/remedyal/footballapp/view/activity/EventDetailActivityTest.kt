package com.remedyal.footballapp.view.activity

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.remedyal.footballapp.R.id.*
import com.remedyal.footballapp.R.string.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventDetailActivityTest {

    private lateinit var eventDetailIdlingResourceList: List<IdlingResource>

    @Rule
    @JvmField
    var eventDetailRule = ActivityTestRule(EventDetailActivity::class.java, false, false)

    @Before
    fun setUp() {
        val i = Intent()
        i.putExtra("id", "576598")
        eventDetailRule.launchActivity(i)

        eventDetailIdlingResourceList = eventDetailRule.activity.getIdlingResources()
        for (item in eventDetailIdlingResourceList) {
            IdlingRegistry.getInstance().register(item)
        }
    }

    @After
    fun tearDown() {
        for (item in eventDetailIdlingResourceList) {
            IdlingRegistry.getInstance().unregister(item)
        }
    }

    @Test
    fun testToggleFavoriteBehaviour() {
        onView(withId(mi_add_to_favorite)).perform(click())
        onView(withText(eventDetailRule.activity.getString(info_favorite_added)))
            .check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(mi_add_to_favorite)).perform(click())
        onView(withText(eventDetailRule.activity.getString(info_favorite_removed)))
            .check(matches(isDisplayed()))
    }
}