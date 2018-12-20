package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.TestContextProvider
import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.model.data.TeamData
import com.remedyal.footballapp.model.response.LeaguesResponse
import com.remedyal.footballapp.model.response.TeamsResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.view.interfaces.TeamsView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TeamsPresenterTest {

    private lateinit var presenter: TeamsPresenter
    private val leagues: MutableList<LeagueData> = mutableListOf()
    private val teams: MutableList<TeamData> = mutableListOf()
    private val leaguesResponse = LeaguesResponse(leagues)
    private val teamsResponse = TeamsResponse(teams)
    private val leagueId = "4328"

    @Mock
    private
    lateinit var view: TeamsView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamsPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetLeagueList(){
        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getLeaguesURL()).await(),
                LeaguesResponse::class.java
            )).thenReturn(leaguesResponse)

            val filteredLeagues:ArrayList<LeagueData> = ArrayList()

            for (i: Int in 0..leaguesResponse.leagues.size.minus(1)) {
                if (leaguesResponse.leagues[i].leagueSport?.toLowerCase()?.contains("soccer") as Boolean) {
                    filteredLeagues.add(leaguesResponse.leagues[i])
                }
            }

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showLeagueList(filteredLeagues)
            Mockito.verify(view).hideLoading()
        }
    }

    @Test
    fun getTeamList() {
        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getTeamsURL(leagueId)).await(),
                    TeamsResponse::class.java
                )
            ).thenReturn(teamsResponse)

            presenter.getTeamList(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeamList(teams)
            Mockito.verify(view).hideLoading()
        }
    }
}