package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.model.response.EventsResponse
import com.remedyal.footballapp.model.response.TeamsResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.util.CoroutineContextProvider
import com.remedyal.footballapp.view.interfaces.EventDetailView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventDetailPresenter(
    private val view: EventDetailView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getEventDetail(eventId: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val eventData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getEventDetailURL(eventId)).await(),
                EventsResponse::class.java
            )

            val homeTeamId = eventData.events[0].homeTeamId
            val awayTeamId = eventData.events[0].awayTeamId

            val homeTeamData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getTeamDetailURL(homeTeamId)).await(),
                TeamsResponse::class.java
            )

            val awayTeamData = gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getTeamDetailURL(awayTeamId)).await(),
                TeamsResponse::class.java
            )

            view.showEventDetail(eventData.events[0], homeTeamData.teams[0].teamBadge, awayTeamData.teams[0].teamBadge)
            view.hideLoading()
        }
    }
}