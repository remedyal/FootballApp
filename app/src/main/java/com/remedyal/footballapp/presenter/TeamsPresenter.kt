package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.model.response.LeaguesResponse
import com.remedyal.footballapp.model.response.TeamsResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.util.CoroutineContextProvider
import com.remedyal.footballapp.view.interfaces.TeamsView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamsPresenter(
    private val view: TeamsView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getLeagueList(){
        view.showLoading()

        GlobalScope.launch(context.main) {
            val leagueData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getLeaguesURL()).await(),
                LeaguesResponse::class.java
            )

            val filteredLeagues:ArrayList<LeagueData> = ArrayList()

            for (i: Int in 0..leagueData.leagues.size.minus(1)) {
                if (leagueData.leagues[i].leagueSport?.toLowerCase()?.contains("soccer") as Boolean) {
                    filteredLeagues.add(leagueData.leagues[i])
                }
            }

            view.showLeagueList(filteredLeagues)
            view.hideLoading()
        }
    }

    fun getTeamList(leagueId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val teamData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getTeamsURL(leagueId)).await(),
                TeamsResponse::class.java
            )

            view.showTeamList(teamData.teams)
            view.hideLoading()
        }
    }
}