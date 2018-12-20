package com.remedyal.footballapp.network

import org.junit.Assert.assertEquals
import org.junit.Test

class TheSportsDBApiTest {

    private val eventId: String = "441613"
    private val leagueId: String = "4328"
    private val teamId:String = "133604"
    private val playerId:String = "34145937"
    private val validLeaguesURL = "https://www.thesportsdb.com/api/v1/json/1/all_leagues.php"
    private val validPastEventsURL = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=$leagueId"
    private val validNextEventsURL = "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=$leagueId"
    private val validEventDetailURL = "https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=$eventId"
    private val validTeamsURL = "https://www.thesportsdb.com/api/v1/json/1/lookup_all_teams.php?id=$leagueId"
    private val validTeamDetailURL = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=$teamId"
    private val validPlayersURL = "https://www.thesportsdb.com/api/v1/json/1/lookup_all_players.php?id=$teamId"
    private val validPlayerDetailURL = "https://www.thesportsdb.com/api/v1/json/1/lookupplayer.php?id=$playerId"

    @Test
    fun testGetLeaguesURL() {
        assertEquals(validLeaguesURL, TheSportsDBApi.getLeaguesURL())
    }

    @Test
    fun testGetPastEventsURL() {
        assertEquals(validPastEventsURL, TheSportsDBApi.getPastEventsURL(leagueId))
    }

    @Test
    fun testGetNextEventsURL() {
        assertEquals(validNextEventsURL, TheSportsDBApi.getNextEventsURL(leagueId))
    }

    @Test
    fun testGetEventDetailURL() {
        assertEquals(validEventDetailURL, TheSportsDBApi.getEventDetailURL(eventId))
    }

    @Test
    fun testGetTeamsURL() {
        assertEquals(validTeamsURL, TheSportsDBApi.getTeamsURL(leagueId))
    }

    @Test
    fun testGetTeamDetailURL() {
        assertEquals(validTeamDetailURL, TheSportsDBApi.getTeamDetailURL(teamId))
    }

    @Test
    fun testGetPlayersURL() {
        assertEquals(validPlayersURL, TheSportsDBApi.getPlayersURL(teamId))
    }

    @Test
    fun testGetPlayerDetailURL() {
        assertEquals(validPlayerDetailURL, TheSportsDBApi.getPlayerDetailURL(playerId))
    }
}