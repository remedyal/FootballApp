package com.remedyal.footballapp.presenter

import com.remedyal.footballapp.TestContextProvider
import com.remedyal.footballapp.model.data.EventData
import com.remedyal.footballapp.model.data.TeamData
import com.remedyal.footballapp.model.response.EventsResponse
import com.remedyal.footballapp.model.response.TeamsResponse
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.network.TheSportsDBApi
import com.remedyal.footballapp.view.interfaces.EventDetailView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EventDetailPresenterTest {

    private lateinit var presenter: EventDetailPresenter
    private val events: MutableList<EventData> = mutableListOf()
    private val homeTeams: MutableList<TeamData> = mutableListOf()
    private val awayTeams: MutableList<TeamData> = mutableListOf()

    private val eventsResponse = EventsResponse(events)
    private val homeTeamsResponse = TeamsResponse(homeTeams)
    private val awayTeamsResponse = TeamsResponse(awayTeams)
    private val eventId = "441613"

    @Mock
    private
    lateinit var view: EventDetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = EventDetailPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetEventDetail() {
        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getEventDetailURL(eventId)).await(),
                    EventsResponse::class.java
                )
            ).thenReturn(eventsResponse)

            val homeTeamName = eventsResponse.events[0].homeTeamName
            val awayTeamName = eventsResponse.events[0].awayTeamName

            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getTeamDetailURL(homeTeamName)).await(),
                    TeamsResponse::class.java
                )
            ).thenReturn(homeTeamsResponse)

            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportsDBApi.getTeamDetailURL(awayTeamName)).await(),
                    TeamsResponse::class.java
                )
            ).thenReturn(awayTeamsResponse)

            presenter.getEventDetail(eventId)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventDetail(events[0], homeTeams[0].teamBadge, awayTeams[0].teamBadge)
            Mockito.verify(view).hideLoading()
        }
    }
}