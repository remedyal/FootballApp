package com.remedyal.footballapp.adapter.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.remedyal.footballapp.model.data.TeamData
import com.squareup.picasso.Picasso
import com.remedyal.footballapp.R.layout.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rv_team_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class TeamsRecyclerAdapter(
    private val context: Context,
    private val teamList: List<TeamData>,
    private val listener: (TeamData)
    -> Unit
) : RecyclerView.Adapter<TeamViewHolder>(), Filterable {

    private var filteredTeamList: List<TeamData> = teamList
    private var recyclerFilter: TeamRecyclerFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder =
        TeamViewHolder(LayoutInflater.from(context).inflate(rv_team_item, parent, false))

    override fun getItemCount(): Int = filteredTeamList.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindItem(filteredTeamList[position], listener)
    }

    override fun getFilter(): Filter {
        if (recyclerFilter == null) {
            recyclerFilter = TeamRecyclerFilter()
        }
        return recyclerFilter as TeamRecyclerFilter
    }

    inner class TeamRecyclerFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                val localFilteredTeamList: ArrayList<TeamData> = ArrayList()

                for (i: Int in 0..teamList.size.minus(1)) {
                    if (teamList[i].teamName?.toLowerCase()?.contains(constraint.toString().toLowerCase()) as Boolean) {
                        localFilteredTeamList.add(teamList[i])
                    }
                }

                results.values = localFilteredTeamList
                results.count = localFilteredTeamList.size
            } else {
                results.values = teamList
                results.count = teamList.size
            }

            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredTeamList = results?.values as List<TeamData>
            notifyDataSetChanged()
        }
    }
}

class TeamViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bindItem(team: TeamData, listener: (TeamData) -> Unit) {
        Picasso.get().load(team.teamBadge).into(itemView.iv_team_badge)
        itemView.tv_team_name.text = team.teamName

        itemView.onClick {
            listener(team)
        }
    }
}