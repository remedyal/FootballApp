package com.remedyal.footballapp.view.interfaces

import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.model.data.TeamData

interface TeamsView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(leagueList: List<LeagueData>)
    fun showTeamList(teamList: List<TeamData>)
}