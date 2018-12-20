package com.remedyal.footballapp.model.response

import com.remedyal.footballapp.model.data.PlayerData

data class PlayersResponse(
    val player: List<PlayerData>,
    val players: List<PlayerData>
)