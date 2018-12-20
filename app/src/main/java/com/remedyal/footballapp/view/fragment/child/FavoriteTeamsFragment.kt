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
import com.remedyal.footballapp.adapter.recycler.FavoriteTeamsRecyclerAdapter
import com.remedyal.footballapp.helper.database
import com.remedyal.footballapp.model.data.FavoriteTeamData
import com.remedyal.footballapp.view.activity.HomeActivity
import com.remedyal.footballapp.view.activity.TeamDetailActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteTeamsFragment : Fragment(), AnkoComponent<Context> {

    private var favoriteTeamList: MutableList<FavoriteTeamData> = mutableListOf()
    private lateinit var rvFavoriteTeams: RecyclerView
    private lateinit var rvFavoriteTeamsAdapter: FavoriteTeamsRecyclerAdapter
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

                rvFavoriteTeams = recyclerView {
                    id = R.id.rv_teams
                    lparams(width = matchParent, height = wrapContent)
                    layoutManager = LinearLayoutManager(ctx)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvFavoriteTeamsAdapter = FavoriteTeamsRecyclerAdapter(
            requireContext(),
            favoriteTeamList
        ) {
            context?.startActivity<TeamDetailActivity>(
                "id" to "${it.teamId}"
            )
        }

        rvFavoriteTeams.adapter = rvFavoriteTeamsAdapter

        swipeRefresh.onRefresh {
            showFavorite()
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    private fun showFavorite() {
        (context as? HomeActivity)?.getTeamsIdlingResource?.increment()

        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTeamData.TABLE_TEAM_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteTeamData>())
            favoriteTeamList.clear()
            favoriteTeamList.addAll(favorite)
            rvFavoriteTeamsAdapter.notifyDataSetChanged()
        }

        (context as? HomeActivity)?.getTeamsIdlingResource?.decrement()
    }
}