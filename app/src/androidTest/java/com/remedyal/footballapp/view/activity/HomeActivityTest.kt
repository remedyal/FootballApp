package com.remedyal.footballapp.view.activity

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.remedyal.footballapp.R.id.*
import org.hamcrest.core.AllOf.allOf
import org.junit.runner.RunWith
import android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA
import com.remedyal.footballapp.model.data.LeagueData
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import com.remedyal.footballapp.view.activity.CustomViewAction.Companion.clickChildViewWithId
import com.remedyal.footballapp.view.activity.CustomViewAction.Companion.typeSearchViewText
import org.junit.*


@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    private lateinit var mDevice: UiDevice
    private lateinit var homeIdlingResourceList: List<IdlingResource>

    @Rule
    @JvmField
    var homeRule = ActivityTestRule(HomeActivity::class.java)

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")
        }
    }

    @Before
    fun setUp() {
        homeIdlingResourceList = homeRule.activity.getIdlingResources()

        for (item in homeIdlingResourceList) {
            IdlingRegistry.getInstance().register(item)
        }

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @After
    fun tearDown() {
        for (item in homeIdlingResourceList) {
            IdlingRegistry.getInstance().unregister(item)
        }
    }

    private fun clickTabLayoutItem(bottomNavId: Int, tabLayoutId: Int, tabItemName: List<String>) {
        onView(withId(bottomNavId)).perform(click())

        for (item in tabItemName) {
            onView(allOf(withText(item), isDescendantOfA(withId(tabLayoutId)))).perform(click())
        }
    }

    private fun clickSpinnerItem(position: Int, id: Int) {
        onView(allOf(isDisplayed(), withId(id))).perform(click())
        onData(allOf(`is`(instanceOf(LeagueData::class.java)))).atPosition(position).perform(click())
    }

    private fun clickRecycleViewItem(position: Int, id: Int) {
        onView(allOf(isDisplayed(), withId(id)))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(position))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    private fun clickCalendarItem(position: Int, recycleId: Int, calendarId: Int) {
        onView(allOf(isDisplayed(), withId(recycleId)))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position, clickChildViewWithId(calendarId)
                )
            )
    }

    private fun clickSearchView(searchId: Int, text: String) {
        onView(allOf(isDisplayed(), withId(searchId))).perform(click()).perform(typeSearchViewText(text))
    }

    @Test
    fun testBottomNavigationBehaviour() {
        onView(withId(bottom_navigation)).check(matches(isDisplayed()))
        onView(withId(mi_teams)).perform(click())
        onView(withId(mi_favorites)).perform(click())
        onView(withId(mi_matches)).perform(click())
    }

    @Test
    fun testTabLayoutBehaviour() {
        clickTabLayoutItem(mi_matches, tl_main, listOf("NEXT", "PAST"))
        clickTabLayoutItem(mi_favorites, tl_main, listOf("MATCHES", "TEAMS"))
    }

    @Test
    fun testSpinnerBehaviour() {
        clickSpinnerItem(12, spinner_leagues)
    }

    @Test
    fun testRecyclerViewBehaviour() {
        clickRecycleViewItem(10, rv_events)
    }

    @Test
    fun testEventSearchViewBehaviour() {
        clickTabLayoutItem(mi_matches, tl_main, listOf("PAST"))
        clickSearchView(mi_search, "Arse")
        clickRecycleViewItem(0, rv_events)
    }

    @Test
    fun testTeamSearchViewBehaviour() {
        onView(withId(mi_teams)).perform(click())
        clickSearchView(mi_search, "Arse")
        clickRecycleViewItem(0, rv_teams)
    }

    @Ignore
    fun testCalendarBehaviour() {
        clickCalendarItem(10, rv_events, iv_add_calendar)
        mDevice.pressBack()
        val button = mDevice.findObject(UiSelector().text("OK"))
        if (button.exists() && button.isEnabled) {
            button.click()
        }
    }

    @Test
    fun testSelectPastEvent() {
        clickTabLayoutItem(mi_matches, tl_main, listOf("PAST"))
        clickSpinnerItem(2, spinner_leagues)
        clickRecycleViewItem(10, rv_events)
    }

    @Test
    fun testSelectNextEvent() {
        clickTabLayoutItem(mi_matches, tl_main, listOf("NEXT"))
        clickSpinnerItem(2, spinner_leagues)
        clickRecycleViewItem(10, rv_events)
    }

    @Test
    fun testSelectTeam() {
        onView(withId(mi_teams)).perform(click())
        clickSpinnerItem(2, spinner_leagues)
        clickRecycleViewItem(10, rv_teams)
    }

    @Test
    fun testSelectFavoriteEvent() {
        testSelectPastEvent()
        Thread.sleep(2000)
        onView(withId(mi_add_to_favorite)).perform(click())
        Espresso.pressBack()
        clickTabLayoutItem(mi_favorites, tl_main, listOf("MATCHES"))
        clickRecycleViewItem(0, rv_events)
        Thread.sleep(2000)
        onView(withId(mi_add_to_favorite)).perform(click())
        Espresso.pressBack()
    }

    @Test
    fun testSelectFavoriteTeam() {
        testSelectTeam()
        Thread.sleep(2000)
        onView(withId(mi_add_to_favorite)).perform(click())
        Espresso.pressBack()
        clickTabLayoutItem(mi_favorites, tl_main, listOf("TEAMS"))
        clickRecycleViewItem(0, rv_teams)
        Thread.sleep(2000)
        onView(withId(mi_add_to_favorite)).perform(click())
        Espresso.pressBack()
    }
}