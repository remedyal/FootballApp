package com.remedyal.footballapp.model.response

import com.remedyal.footballapp.model.data.EventData

data class EventsResponse(
    val events: List<EventData>
)