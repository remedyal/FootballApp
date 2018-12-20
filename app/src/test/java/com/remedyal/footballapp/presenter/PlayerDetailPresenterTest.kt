package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.TestContextProvider
import com.remedyal.footballapp.model.data.PlayerData
import com.remedyal.footballapp.model.response.PlayersResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.view.interfaces.PlayerDetailView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlayerDetailPresenterTest {

    private lateinit var presenter: PlayerDetailPresenter
    private val player: MutableList<PlayerData> = mutableListOf()
    private val players: MutableList<PlayerData> = mutableListOf()
    private val playerResponse = PlayersResponse(player, players)
    private val playerId = "34145937"

    @Mock
    private
    lateinit var view: PlayerDetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PlayerDetailPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetPlayerDetail() {
        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getPlayerDetailURL(playerId)).await(),
                    PlayersResponse::class.java
                )
            ).thenReturn(playerResponse)

            presenter.getPlayerDetail(playerId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showPlayerDetail(player[0])
            Mockito.verify(view).hideLoading()
        }
    }
}