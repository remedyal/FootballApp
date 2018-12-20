package com.remedyal.footballapp.view.activity

import android.database.sqlite.SQLiteConstraintException
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.R.color.*
import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.db.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.remedyal.footballapp.R.id.*
import com.remedyal.footballapp.R.string.*
import com.remedyal.footballapp.R.drawable.*
import com.remedyal.footballapp.R.color.*
import com.remedyal.footballapp.R.layout.activity_event_detail
import com.remedyal.footballapp.R.menu.*
import com.remedyal.footballapp.helper.database
import com.remedyal.footballapp.model.data.EventData
import com.remedyal.footballapp.model.data.FavoriteEventData
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.presenter.EventDetailPresenter
import com.remedyal.footballapp.util.*
import com.remedyal.footballapp.view.interfaces.EventDetailView
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import java.util.*

class EventDetailActivity : AppCompatActivity(), EventDetailView {

    private lateinit var eventId: String
    private lateinit var item: EventData
    private lateinit var presenter: EventDetailPresenter
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private val getEventDetailIdlingResource = CountingIdlingResource("Get_Event_Detail")

    fun getIdlingResources(): List<CountingIdlingResource> {
        return listOf(getEventDetailIdlingResource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_event_detail)

        val intent = intent
        eventId = intent.getStringExtra("id")

        tb_parent.title = getString(title_activity_event_detail)
        setSupportActionBar(tb_parent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteState()
        val request = ApiRepository()
        val gson = Gson()
        presenter = EventDetailPresenter(this, request, gson)
        getEventDetailIdlingResource.increment()
        presenter.getEventDetail(eventId)

        swipe_refresh.setColorSchemeResources(colorAccent, holo_green_light, holo_orange_light, holo_red_light)
        swipe_refresh.onRefresh {
            getEventDetailIdlingResource.increment()
            getEventDetailIdlingResource.dumpStateToLogs()
            presenter.getEventDetail(eventId)
        }
    }

    private fun favoriteState() {
        getEventDetailIdlingResource.increment()

        database.use {
            val result = select(FavoriteEventData.TABLE_EVENT_FAVORITE)
                .whereArgs(
                    "(EVENT_ID = {id})",
                    "id" to eventId
                )

            val favorite = result.parseList(classParser<FavoriteEventData>())
            if (!favorite.isEmpty()) isFavorite = true
        }

        getEventDetailIdlingResource.decrement()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(menu_favorite, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            home -> {
                finish()
                true
            }

            mi_add_to_favorite -> {
                getEventDetailIdlingResource.increment()

                try {
                    if (isFavorite) removeFromFavorite() else addToFavorite()
                    isFavorite = !isFavorite
                    setFavorite()
                } catch (e: Exception) {
                    val message = when (e) {
                        is SQLiteConstraintException -> getString(err_database)
                        is UninitializedPropertyAccessException -> getString(err_components)
                        else -> getString(err_unknown)
                    }

                    swipe_refresh.snackbar(message).show()

                }

                getEventDetailIdlingResource.decrement()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite() {

        if (this::item.isInitialized) {
            database.use {
                insert(
                    FavoriteEventData.TABLE_EVENT_FAVORITE,
                    FavoriteEventData.EVENT_ID to item.eventId,
                    FavoriteEventData.EVENT_DATE to item.eventDate,
                    FavoriteEventData.EVENT_TIME to item.eventTime,
                    FavoriteEventData.HOME_TEAM to item.homeTeamName,
                    FavoriteEventData.AWAY_TEAM to item.awayTeamName,
                    FavoriteEventData.HOME_SCORE to item.homeScore,
                    FavoriteEventData.AWAY_SCORE to item.awayScore
                )
            }

            swipe_refresh.snackbar(getString(info_favorite_added)).show()
        } else {
            throw UninitializedPropertyAccessException()
        }

    }

    @Throws(Exception::class)
    private fun removeFromFavorite() {

        database.use {
            delete(
                FavoriteEventData.TABLE_EVENT_FAVORITE, "(EVENT_ID = {id})",
                "id" to eventId
            )
        }

        swipe_refresh.snackbar(getString(info_favorite_removed)).show()
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    override fun showLoading() {
        progress_circular.visible()
    }

    override fun hideLoading() {
        progress_circular.invisible()
    }

    override fun showEventDetail(event: EventData, homeTeamBadge: String?, awayTeamBadge: String?) {
        swipe_refresh.isRefreshing = false
        item = event
        val eventCompleteDate: Date? = toGMTDate(item.eventDate.toString(), item.eventTime.toString())
        tv_event_date.text = eventCompleteDate.toDateText()
        tv_event_time.text = eventCompleteDate.toTimeText()
        Picasso.get().load(homeTeamBadge).into(iv_home_badge)
        Picasso.get().load(awayTeamBadge).into(iv_away_badge)
        tv_home_name.text = item.homeTeamName
        tv_away_name.text = item.awayTeamName
        tv_home_formation.text = item.homeFormation
        tv_away_formation.text = item.awayFormation
        tv_home_score.text = item.homeScore.toText()
        tv_away_score.text = item.awayScore.toText()
        tv_home_goal_details.text = item.homeGoalDetails.toGoalDetailsText()
        tv_away_goal_details.text = item.awayGoalDetails.toGoalDetailsText()
        tv_home_shots.text = item.homeShots.toText()
        tv_away_shots.text = item.awayShots.toText()
        tv_home_goalkeeper.text = item.homeGoalkeeper.toLineupText()
        tv_away_goalkeeper.text = item.awayGoalkeeper.toLineupText()
        tv_home_defense.text = item.homeDefense.toLineupText()
        tv_away_defense.text = item.awayDefense.toLineupText()
        tv_home_midfield.text = item.homeMidfield.toLineupText()
        tv_away_midfield.text = item.awayMidfield.toLineupText()
        tv_home_forward.text = item.homeForward.toLineupText()
        tv_away_forward.text = item.awayForward.toLineupText()
        tv_home_substitutes.text = item.homeSubstitutes.toLineupText()
        tv_away_substitutes.text = item.awaySubstitutes.toLineupText()
        getEventDetailIdlingResource.decrement()
    }
}