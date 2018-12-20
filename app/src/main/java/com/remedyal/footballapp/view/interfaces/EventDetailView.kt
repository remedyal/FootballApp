package com.remedyal.footballapp.view.interfaces

import com.remedyal.footballapp.model.data.EventData

interface EventDetailView {
    fun showLoading()
    fun hideLoading()
    fun showEventDetail(event: EventData, homeTeamBadge: String?, awayTeamBadge: String?)
}