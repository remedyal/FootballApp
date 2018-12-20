package com.remedyal.footballapp.view.interfaces

import com.remedyal.footballapp.model.data.EventData
import com.remedyal.footballapp.model.data.LeagueData

interface EventsView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(leagueList: List<LeagueData>)
    fun showEventList(eventList: List<EventData>)
}