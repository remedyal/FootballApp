package com.remedyal.footballapp.network

import com.remedyal.footballapp.BuildConfig

object TheSportsDBApi {

    private const val MAIN_URL = BuildConfig.BASE_URL

    fun getLeaguesURL(): String {
        return MAIN_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/all_leagues.php"
    }
    
    fun getPastEventsURL(leagueId: String?): String {
        return MAIN_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/eventspastleague.php?id=" + leagueId
    }

    fun getNextEventsURL(leagueId: String?): String {
        return MAIN_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/eventsnextleague.php?id=" + leagueId
    }

    fun getEventDetailURL(eventId: String?): String {
        return MAIN_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/lookupevent.php?id=" + eventId
    }

    fun getTeamsURL(leagueId: String?): String {
        return MAIN_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/lookup_all_teams.php?id=" + leagueId
    }

    fun getTeamDetailURL(teamId: String?): String {
        return MAIN_URL+ "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/lookupteam.php?id=" + teamId
    }

    fun getPlayersURL(teamId: String?):String{
        return BuildConfig.BASE_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/lookup_all_players.php?id=" + teamId
    }

    fun getPlayerDetailURL(playerId: String?): String {
        return MAIN_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/lookupplayer.php?id=" + playerId
    }
}