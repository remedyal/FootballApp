package com.remedyal.footballapp.view.fragment.child

import android.R.color.*
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.remedyal.footballapp.R
import com.remedyal.footballapp.R.*
import com.remedyal.footballapp.adapter.recycler.PlayersRecyclerAdapter
import com.remedyal.footballapp.model.data.PlayerData
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.presenter.PlayersPresenter
import com.remedyal.footballapp.util.invisible
import com.remedyal.footballapp.util.visible
import com.remedyal.footballapp.view.activity.PlayerDetailActivity
import com.remedyal.footballapp.view.activity.TeamDetailActivity
import com.remedyal.footballapp.view.interfaces.PlayersView
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PlayersFragment : Fragment(), AnkoComponent<Context>, PlayersView {

    companion object {
        fun newInstance(teamId: String): PlayersFragment {
            val fragment = PlayersFragment()
            val args = Bundle()
            args.putString("teamId", teamId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var teamId: String
    private lateinit var presenter: PlayersPresenter
    private var playerList: MutableList<PlayerData> = mutableListOf()
    private lateinit var rvPlayers: RecyclerView
    private lateinit var rvPlayersAdapter: PlayersRecyclerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        teamId = arguments?.getString("teamId").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        verticalLayout {
            lparams(width = matchParent, height = wrapContent)
            padding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    color.colorAccent,
                    holo_green_light,
                    holo_orange_light,
                    holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    rvPlayers = recyclerView {
                        id = R.id.rv_players
                        layoutManager = LinearLayoutManager(ctx)
                    }.lparams(width = matchParent, height = wrapContent)

                    progressBar = progressBar().lparams {
                        centerInParent()
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvPlayersAdapter = PlayersRecyclerAdapter(requireContext(), playerList) {
            context?.startActivity<PlayerDetailActivity>("id" to "${it.playerId}")
        }

        rvPlayers.adapter = rvPlayersAdapter
        presenter = PlayersPresenter(this, ApiRepository(), Gson())
        (context as? TeamDetailActivity)?.getPlayersIdlingResource?.increment()
        presenter.getPlayerList(teamId)

        swipeRefresh.onRefresh {
            (context as? TeamDetailActivity)?.getPlayersIdlingResource?.increment()
            presenter.getPlayerList(teamId)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showPlayerList(playerList: List<PlayerData>) {
        swipeRefresh.isRefreshing = false
        this.playerList.clear()
        this.playerList.addAll(playerList)
        rvPlayersAdapter.notifyDataSetChanged()
        (context as? TeamDetailActivity)?.getPlayersIdlingResource?.decrement()
    }
}