package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.TestContextProvider
import com.remedyal.footballapp.model.data.TeamData
import com.remedyal.footballapp.model.response.TeamsResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.view.interfaces.TeamDetailView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TeamDetailPresenterTest {

    private lateinit var presenter: TeamDetailPresenter
    private val teams: MutableList<TeamData> = mutableListOf()
    private val teamsResponse = TeamsResponse(teams)
    private val teamId = "133604"

    @Mock
    private
    lateinit var view: TeamDetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamDetailPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun getTeamDetail() {
        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getTeamDetailURL(teamId)).await(),
                    TeamsResponse::class.java
                )
            ).thenReturn(teamsResponse)

            presenter.getTeamDetail(teamId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeamDetail(teams[0])
            Mockito.verify(view).hideLoading()
        }
    }
}