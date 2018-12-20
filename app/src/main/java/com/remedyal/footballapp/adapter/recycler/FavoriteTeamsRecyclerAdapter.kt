package com.remedyal.footballapp.adapter.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remedyal.footballapp.R.layout.rv_team_item
import com.remedyal.footballapp.model.data.FavoriteTeamData
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rv_team_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class FavoriteTeamsRecyclerAdapter(
    private val context: Context,
    private val favoriteTeamList: List<FavoriteTeamData>,
    private val listener: (FavoriteTeamData) -> Unit
) :
    RecyclerView.Adapter<FavoriteTeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoriteTeamViewHolder(LayoutInflater.from(context).inflate(rv_team_item, parent, false))

    override fun getItemCount(): Int = favoriteTeamList.size

    override fun onBindViewHolder(holder: FavoriteTeamViewHolder, position: Int) {
        holder.bindItem(favoriteTeamList[position], listener)
    }
}

class FavoriteTeamViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bindItem(favoriteTeam: FavoriteTeamData, listener: (FavoriteTeamData) -> Unit) {
        Picasso.get().load(favoriteTeam.teamBadge).into(itemView.iv_team_badge)
        itemView.tv_team_name.text = favoriteTeam.teamName

        itemView.onClick {
            listener(favoriteTeam)
        }
    }
}