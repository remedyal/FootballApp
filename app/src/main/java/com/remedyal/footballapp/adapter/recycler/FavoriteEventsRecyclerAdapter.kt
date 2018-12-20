package com.remedyal.footballapp.adapter.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remedyal.footballapp.R.layout.rv_event_item
import com.remedyal.footballapp.model.data.FavoriteEventData
import com.remedyal.footballapp.util.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rv_event_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


class FavoriteEventsRecyclerAdapter(
    private val context: Context,
    private val favoriteEventList: List<FavoriteEventData>,
    private val listener: (FavoriteEventData) -> Unit
) :
    RecyclerView.Adapter<FavoriteEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoriteEventViewHolder(LayoutInflater.from(context).inflate(rv_event_item, parent, false))

    override fun getItemCount(): Int = favoriteEventList.size

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bindItem(favoriteEventList[position], listener)
    }
}

class FavoriteEventViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bindItem(favoriteEvent: FavoriteEventData, listener: (FavoriteEventData) -> Unit) {
        val eventCompleteDate: Date? = toGMTDate(favoriteEvent.eventDate.toString(), favoriteEvent.eventTime.toString())
        itemView.tv_event_date.text = eventCompleteDate.toDateText()
        itemView.tv_event_time.text = eventCompleteDate.toTimeText()
        itemView.tv_home_team.text = favoriteEvent.homeTeamName
        itemView.tv_home_score.text = favoriteEvent.homeTeamScore?.toText()
        itemView.tv_away_team.text = favoriteEvent.awayTeamName
        itemView.tv_away_score.text = favoriteEvent.awayTeamScore?.toText()
        itemView.iv_add_calendar.visibility = View.INVISIBLE

        itemView.onClick {
            listener(favoriteEvent)
        }
    }
}