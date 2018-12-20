package com.remedyal.footballapp.view.activity

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import com.remedyal.footballapp.R.id.*
import com.remedyal.footballapp.R.string.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TeamDetailActivityTest {

    private lateinit var teamDetailIdlingResourceList: List<IdlingResource>

    @Rule
    @JvmField
    var teamDetailRule = ActivityTestRule(TeamDetailActivity::class.java, false, false)

    @Before
    fun setUp() {
        val i = Intent()
        i.putExtra("id", "133604")
        teamDetailRule.launchActivity(i)

        teamDetailIdlingResourceList = teamDetailRule.activity.getIdlingResources()
        for (item in teamDetailIdlingResourceList) {
            IdlingRegistry.getInstance().register(item)
        }
    }

    @After
    fun tearDown() {
        for (item in teamDetailIdlingResourceList) {
            IdlingRegistry.getInstance().unregister(item)
        }
    }

    private fun clickTabLayoutItem(tabLayoutId: Int, tabItemName: List<String>) {
        for (item in tabItemName) {
            onView(
                allOf(withText(item), isDescendantOfA(withId(tabLayoutId)))
            ).perform(click())
        }
    }

    private fun clickRecycleViewItem(position: Int, id: Int) {
        onView(allOf(isDisplayed(), withId(id)))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(position))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    @Test
    fun testToggleFavoriteBehaviour() {
        onView(withId(mi_add_to_favorite)).perform(click())
        onView(withText(teamDetailRule.activity.getString(info_favorite_added)))
            .check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(mi_add_to_favorite)).perform(click())
        onView(withText(teamDetailRule.activity.getString(info_favorite_removed)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTabLayoutBehaviour() {
        clickTabLayoutItem(tl_main, listOf("PLAYERS", "OVERVIEW"))
    }

    @Test
    fun testSelectPlayer() {
        clickTabLayoutItem(tl_main, listOf("PLAYERS"))
        clickRecycleViewItem(2, rv_players)
    }
}