package com.remedyal.footballapp.view.activity

import android.os.Bundle
import android.support.design.R.id.home
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.remedyal.footballapp.model.data.PlayerData
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.presenter.PlayerDetailPresenter
import com.remedyal.footballapp.util.invisible
import com.remedyal.footballapp.util.visible
import com.remedyal.footballapp.view.interfaces.PlayerDetailView
import com.google.gson.Gson
import org.jetbrains.anko.support.v4.onRefresh
import com.remedyal.footballapp.R.layout.activity_player_detail
import com.remedyal.footballapp.util.toPlayerStatText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player_detail.*


class PlayerDetailActivity : AppCompatActivity(), PlayerDetailView {

    private lateinit var playerId: String
    private lateinit var presenter: PlayerDetailPresenter
    private val getPlayerDetailIdlingResource = CountingIdlingResource("Get_Player_Detail")

    fun getIdlingResources(): List<CountingIdlingResource> {
        return listOf(getPlayerDetailIdlingResource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_player_detail)

        setSupportActionBar(tb_parent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playerId = intent.getStringExtra("id")
        presenter = PlayerDetailPresenter(this, ApiRepository(), Gson())
        getPlayerDetailIdlingResource.increment()
        presenter.getPlayerDetail(playerId)

        swipe_refresh.onRefresh {
            getPlayerDetailIdlingResource.increment()
            presenter.getPlayerDetail(playerId)
        }
    }

    override fun showLoading() {
        progress_circular.visible()
    }

    override fun hideLoading() {
        progress_circular.invisible()
    }

    override fun showPlayerDetail(player: PlayerData) {
        swipe_refresh.isRefreshing = false
        Picasso.get().load(player.playerFanart).into(iv_player_fanart)
        val actionBar = supportActionBar
        actionBar?.title = player.playerName
        tv_player_weight.text = player.playerWeight?.toPlayerStatText()
        tv_player_height.text = player.playerHeight?.toPlayerStatText()
        tv_player_position.text = player.playerPosition
        tv_player_description.text = player.playerDescription
        getPlayerDetailIdlingResource.decrement()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}