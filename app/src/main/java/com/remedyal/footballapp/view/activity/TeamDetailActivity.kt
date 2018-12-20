package com.remedyal.footballapp.view.activity

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.remedyal.footballapp.R
import com.remedyal.footballapp.R.string.*
import com.remedyal.footballapp.adapter.pager.TeamDetailPagerAdapter
import com.remedyal.footballapp.helper.database
import com.remedyal.footballapp.model.data.FavoriteTeamData
import com.remedyal.footballapp.model.data.TeamData
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.presenter.TeamDetailPresenter
import com.remedyal.footballapp.util.invisible
import com.remedyal.footballapp.util.visible
import com.remedyal.footballapp.view.interfaces.TeamDetailView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class TeamDetailActivity : AppCompatActivity(), TeamDetailView {

    private lateinit var teamId: String
    private lateinit var team: TeamData
    private lateinit var presenter: TeamDetailPresenter
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private val getTeamDetailIdlingResource = CountingIdlingResource("Get_Team_Detail")
    val getPlayersIdlingResource = CountingIdlingResource("Get_Players")

    fun getIdlingResources(): List<CountingIdlingResource> {
        return listOf(getTeamDetailIdlingResource, getPlayersIdlingResource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        teamId = intent.getStringExtra("id")

        val toolbar = findViewById<Toolbar>(R.id.tb_parent)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        favoriteState()
        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamDetailPresenter(this, request, gson)
        getTeamDetailIdlingResource.increment()
        presenter.getTeamDetail(teamId)
    }

    private fun favoriteState() {
        getTeamDetailIdlingResource.increment()

        database.use {
            val result = select(FavoriteTeamData.TABLE_TEAM_FAVORITE)
                .whereArgs(
                    "(TEAM_ID = {id})",
                    "id" to teamId
                )
            val favorite = result.parseList(classParser<FavoriteTeamData>())
            if (!favorite.isEmpty()) isFavorite = true
        }

        getTeamDetailIdlingResource.decrement()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }

            R.id.mi_add_to_favorite -> {
                getTeamDetailIdlingResource.increment()

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

                    cl_parent.snackbar(message).show()
                }

                getTeamDetailIdlingResource.decrement()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite() {
        if (this::team.isInitialized) {
            database.use {
                insert(
                    FavoriteTeamData.TABLE_TEAM_FAVORITE,
                    FavoriteTeamData.TEAM_ID to team.teamId,
                    FavoriteTeamData.TEAM_NAME to team.teamName,
                    FavoriteTeamData.TEAM_BADGE to team.teamBadge
                )
            }

            cl_parent.snackbar(getString(info_favorite_added)).show()
        } else {
            throw UninitializedPropertyAccessException()
        }
    }

    @Throws(Exception::class)
    private fun removeFromFavorite() {
        database.use {
            delete(
                FavoriteTeamData.TABLE_TEAM_FAVORITE, "(TEAM_ID = {id})",
                "id" to teamId
            )
        }
        cl_parent.snackbar(getString(info_favorite_removed)).show()
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    override fun showLoading() {
        progress_circular.visible()
    }

    override fun hideLoading() {
        progress_circular.invisible()
    }

    override fun showTeamDetail(team: TeamData) {
        this.team = team
        Picasso.get().load(team.teamBadge).into(iv_team_badge)
        tv_team_name.text = team.teamName
        tv_team_year.text = team.teamYear.toString()
        tv_team_stadium.text = team.teamStadium
        viewpager.adapter =
                TeamDetailPagerAdapter(supportFragmentManager, this, team.teamDescription.toString(), teamId)
        tl_main.setupWithViewPager(viewpager)
        getTeamDetailIdlingResource.decrement()
    }
}