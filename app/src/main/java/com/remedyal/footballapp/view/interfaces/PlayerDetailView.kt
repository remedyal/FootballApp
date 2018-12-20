package com.remedyal.footballapp.view.interfaces

import com.remedyal.footballapp.model.data.PlayerData

interface PlayerDetailView {
    fun showLoading()
    fun hideLoading()
    fun showPlayerDetail(player: PlayerData)
}