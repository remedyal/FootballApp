package com.remedyal.footballapp.adapter.recycler

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract.Events
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rv_event_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import com.remedyal.footballapp.R.string.*
import com.remedyal.footballapp.R.layout.*
import com.remedyal.footballapp.model.data.EventData
import com.remedyal.footballapp.util.*


class EventsRecyclerAdapter(
    private val context: Context,
    private val eventList: List<EventData>,
    private val listener: (EventData) -> Unit
) :
    RecyclerView.Adapter<EventViewHolder>(), Filterable {

    private var filteredEventList: List<EventData> = eventList
    private var recyclerFilter: EventRecyclerFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventViewHolder(LayoutInflater.from(context).inflate(rv_event_item, parent, false), context)

    override fun getItemCount(): Int = filteredEventList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindItem(filteredEventList[position], listener)
    }

    override fun getFilter(): Filter {
        if (recyclerFilter == null) {
            recyclerFilter = EventRecyclerFilter()
        }
        return recyclerFilter as EventRecyclerFilter
    }

    inner class EventRecyclerFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                val localFilteredEventList: ArrayList<EventData> = ArrayList()

                for (i: Int in 0..eventList.size.minus(1)) {
                    if (eventList[i].homeTeamName?.toLowerCase()?.contains(constraint.toString().toLowerCase()) as Boolean) {
                        localFilteredEventList.add(eventList[i])
                    }
                }

                results.values = localFilteredEventList
                results.count = localFilteredEventList.size
            } else {
                results.values = eventList
                results.count = eventList.size
            }

            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredEventList = results?.values as List<EventData>
            notifyDataSetChanged()
        }
    }
}

class EventViewHolder(override val containerView: View, private val context: Context) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bindItem(event: EventData, listener: (EventData) -> Unit) {
        val eventCompleteDate: Date? = toGMTDate(event.eventDate.toString(), event.eventTime.toString())
        itemView.tv_event_date.text = eventCompleteDate.toDateText()
        itemView.tv_event_time.text = eventCompleteDate.toTimeText()
        itemView.tv_home_team.text = event.homeTeamName
        itemView.tv_home_score.text = event.homeScore.toText()
        itemView.tv_away_team.text = event.awayTeamName
        itemView.tv_away_score.text = event.awayScore.toText()

        itemView.iv_add_calendar.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    eventCompleteDate?.time
                )
                .putExtra(
                    CalendarContract.EXTRA_EVENT_END_TIME,
                    eventCompleteDate?.time?.plus(120 * 60 * 1000)
                )

                .putExtra(Events.TITLE, "${event.homeTeamName} ${context.getString(versus)} ${event.awayTeamName}")
                .putExtra(
                    Events.DESCRIPTION,
                    "${context.getString(calendar_desc)} ${event.homeTeamName} ${context.getString(versus)} ${event.awayTeamName}"
                )

            context.startActivity(intent)
        }

        if (Date().before(eventCompleteDate)) {
            itemView.iv_add_calendar.visibility = View.VISIBLE
        } else {
            itemView.iv_add_calendar.visibility = View.INVISIBLE
        }

        itemView.onClick {
            listener(event)
        }
    }
}