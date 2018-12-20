package com.remedyal.footballapp.view.fragment.child

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.R.color.*
import com.remedyal.footballapp.R
import com.remedyal.footballapp.R.color.colorAccent
import com.remedyal.footballapp.adapter.recycler.FavoriteEventsRecyclerAdapter
import com.remedyal.footballapp.helper.database
import com.remedyal.footballapp.model.data.FavoriteEventData
import com.remedyal.footballapp.view.activity.EventDetailActivity
import com.remedyal.footballapp.view.activity.HomeActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteEventsFragment : Fragment(), AnkoComponent<Context> {

    private var favoriteEventList: MutableList<FavoriteEventData> = mutableListOf()
    private lateinit var rvFavoriteEvents: RecyclerView
    private lateinit var rvFavoriteEventsAdapter: FavoriteEventsRecyclerAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    colorAccent,
                    holo_green_light,
                    holo_orange_light,
                    holo_red_light
                )

                rvFavoriteEvents = recyclerView {
                    id = R.id.rv_events
                    lparams(width = matchParent, height = wrapContent)
                    layoutManager = LinearLayoutManager(ctx)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvFavoriteEventsAdapter = FavoriteEventsRecyclerAdapter(
            requireContext(),
            favoriteEventList
        ) {
            context?.startActivity<EventDetailActivity>(
                "id" to "${it.eventId}"
            )
        }

        rvFavoriteEvents.adapter = rvFavoriteEventsAdapter

        swipeRefresh.onRefresh {
            showFavorite()
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    private fun showFavorite() {
        (context as? HomeActivity)?.getEventsIdlingResource?.increment()

        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteEventData.TABLE_EVENT_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteEventData>())
            favoriteEventList.clear()
            favoriteEventList.addAll(favorite)
            rvFavoriteEventsAdapter.notifyDataSetChanged()
        }

        (context as? HomeActivity)?.getEventsIdlingResource?.decrement()
    }
}