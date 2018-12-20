package com.remedyal.footballapp.view.interfaces

import com.remedyal.footballapp.model.data.PlayerData

interface PlayersView {
    fun showLoading()
    fun hideLoading()
    fun showPlayerList(playerList: List<PlayerData>)
}