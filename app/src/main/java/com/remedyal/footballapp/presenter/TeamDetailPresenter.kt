package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.model.response.TeamsResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.util.CoroutineContextProvider
import com.remedyal.footballapp.view.interfaces.TeamDetailView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamDetailPresenter(
    private val view: TeamDetailView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeamDetail(teamId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getTeamDetailURL(teamId)).await(),
                TeamsResponse::class.java
            )

            view.showTeamDetail(data.teams[0])
            view.hideLoading()
        }
    }
}