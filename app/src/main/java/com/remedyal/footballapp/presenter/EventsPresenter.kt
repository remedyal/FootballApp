package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.model.response.EventsResponse
import com.remedyal.footballapp.model.response.LeaguesResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.util.CoroutineContextProvider
import com.remedyal.footballapp.view.interfaces.EventsView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EventsPresenter(
    private val view: EventsView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getLeagueList(){
        view.showLoading()

        GlobalScope.launch(context.main) {
            val eventData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getLeaguesURL()).await(),
                LeaguesResponse::class.java
            )

            val filteredLeagues:ArrayList<LeagueData> = ArrayList()

            for (i: Int in 0..eventData.leagues.size.minus(1)) {
                if (eventData.leagues[i].leagueSport?.toLowerCase()?.contains("soccer") as Boolean) {
                    filteredLeagues.add(eventData.leagues[i])
                }
            }

            view.showLeagueList(filteredLeagues)
            view.hideLoading()
        }

    }

    fun getPastEventList(leagueId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val eventData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getPastEventsURL(leagueId)).await(),
                EventsResponse::class.java
            )

            view.showEventList(eventData.events)
            view.hideLoading()
        }
    }

    fun getNextEventList(leagueId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val eventData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getNextEventsURL(leagueId)).await(),
                EventsResponse::class.java
            )

            view.showEventList(eventData.events)
            view.hideLoading()
        }
    }
}