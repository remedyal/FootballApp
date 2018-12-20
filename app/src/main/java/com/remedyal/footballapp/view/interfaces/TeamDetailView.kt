package com.remedyal.footballapp.view.interfaces

import com.remedyal.footballapp.model.data.TeamData

interface TeamDetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamDetail(team: TeamData)
}