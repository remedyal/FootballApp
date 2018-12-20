package com.remedyal.footballapp.adapter.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.remedyal.footballapp.R.layout.*
import com.remedyal.footballapp.model.data.PlayerData
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlinx.android.synthetic.main.rv_player_item.view.*

class PlayersRecyclerAdapter(
    private val context: Context,
    private val playerList: List<PlayerData>,
    private val listener: (PlayerData) -> Unit
) : RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
        PlayerViewHolder(LayoutInflater.from(context).inflate(rv_player_item, parent, false))

    override fun getItemCount(): Int = playerList.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindItem(playerList[position], listener)
    }
}

class PlayerViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bindItem(player: PlayerData, listener: (PlayerData) -> Unit) {
        Picasso.get().load(player.playerCutout).into(itemView.iv_player_cutout)
        itemView.tv_player_name.text = player.playerName
        itemView.tv_player_position.text = player.playerPosition

        itemView.onClick {
            listener(player)
        }
    }
}
