package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.TestContextProvider
import com.remedyal.footballapp.model.data.EventData
import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.model.response.EventsResponse
import com.remedyal.footballapp.model.response.LeaguesResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.view.interfaces.EventsView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EventsPresenterTest {

    private lateinit var presenter: EventsPresenter
    private val leagues: MutableList<LeagueData> = mutableListOf()
    private val events: MutableList<EventData> = mutableListOf()
    private val leaguesResponse = LeaguesResponse(leagues)
    private val eventsResponse = EventsResponse(events)
    private val leagueId = "4328"

    @Mock
    private
    lateinit var view: EventsView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = EventsPresenter(view, apiRepository, gson, TestContextProvider())
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
    fun testGetPastEventList() {
        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getPastEventsURL(leagueId)).await(),
                EventsResponse::class.java
            )).thenReturn(eventsResponse)

            presenter.getPastEventList(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventList(events)
            Mockito.verify(view).hideLoading()
        }
    }

    @Test
    fun testGetNextEventList() {
        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(
                apiRepository
                    .doRequest(TheSportsDBApi.getNextEventsURL(leagueId)).await(),
                EventsResponse::class.java
            )).thenReturn(eventsResponse)

            presenter.getNextEventList(leagueId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventList(events)
            Mockito.verify(view).hideLoading()
        }
    }
}