package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.model.response.PlayersResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.util.CoroutineContextProvider
import com.remedyal.footballapp.view.interfaces.PlayersView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayersPresenter(
    private val view: PlayersView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getPlayerList(teamId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val playerData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getPlayersURL(teamId)).await(),
                PlayersResponse::class.java
            )

            view.showPlayerList(playerData.player)
            view.hideLoading()
        }
    }
}