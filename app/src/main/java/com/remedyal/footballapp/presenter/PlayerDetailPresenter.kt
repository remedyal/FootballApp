package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.model.response.PlayersResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.util.CoroutineContextProvider
import com.remedyal.footballapp.view.interfaces.PlayerDetailView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayerDetailPresenter(
    private val view: PlayerDetailView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getPlayerDetail(playerId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val playerData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getPlayerDetailURL(playerId)).await(),
                PlayersResponse::class.java
            )

            view.showPlayerDetail(playerData.players[0])
            view.hideLoading()
        }
    }
}