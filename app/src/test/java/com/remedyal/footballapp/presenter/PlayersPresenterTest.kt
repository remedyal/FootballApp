package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.TestContextProvider
import com.remedyal.footballapp.model.data.PlayerData
import com.remedyal.footballapp.model.response.PlayersResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.view.interfaces.PlayersView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlayersPresenterTest {

    private lateinit var presenter: PlayersPresenter
    private val player: MutableList<PlayerData> = mutableListOf()
    private val players: MutableList<PlayerData> = mutableListOf()
    private val playersResponse = PlayersResponse(player, players)
    private val teamId = "133604"

    @Mock
    private
    lateinit var view: PlayersView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PlayersPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetPlayerList() {
        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getPlayersURL(teamId)).await(),
                    PlayersResponse::class.java
                )
            ).thenReturn(playersResponse)

            presenter.getPlayerList(teamId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showPlayerList(players)
            Mockito.verify(view).hideLoading()
        }
    }
}