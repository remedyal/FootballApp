package com.remedyal.footballapp.view.fragment.child

import android.R.layout.*
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.widget.*
import android.R.color.*
import android.view.*
import com.remedyal.footballapp.R
import com.remedyal.footballapp.adapter.recycler.TeamsRecyclerAdapter
import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.model.data.TeamData
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.presenter.TeamsPresenter
import com.remedyal.footballapp.util.invisible
import com.remedyal.footballapp.util.visible
import com.remedyal.footballapp.view.activity.TeamDetailActivity
import com.remedyal.footballapp.view.interfaces.TeamsView
import com.remedyal.footballapp.R.color.*
import com.remedyal.footballapp.view.activity.HomeActivity
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamsFragment : Fragment(), AnkoComponent<Context>, TeamsView {

    private lateinit var leagueId: String
    private var leagueList: MutableList<LeagueData> = mutableListOf()
    private lateinit var presenter: TeamsPresenter
    private var teamList: MutableList<TeamData> = mutableListOf()
    private lateinit var rvTeams: RecyclerView
    private lateinit var rvTeamsAdapter: TeamsRecyclerAdapter
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<*>
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams (width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            spinner = spinner {
                id = R.id.spinner_leagues
            }
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    colorAccent,
                    holo_green_light,
                    holo_orange_light,
                    holo_red_light)

                relativeLayout{
                    lparams (width = matchParent, height = wrapContent)

                    rvTeams = recyclerView {
                        id = R.id.rv_teams
                        lparams (width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams{
                        centerHorizontally()
                    }
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater?.inflate(R.menu.menu_search, menu)
        val searchView = SearchView((context as HomeActivity).supportActionBar?.themedContext ?: context)
        searchView.fitsSystemWindows = true
        menu.findItem(R.id.mi_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                rvTeamsAdapter.filter.filter(p0)
                return false
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        rvTeamsAdapter = TeamsRecyclerAdapter(requireContext(),teamList) {
            context?.startActivity<TeamDetailActivity>("id" to "${it.teamId}")
        }

        rvTeams.adapter = rvTeamsAdapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamsPresenter(this,request,gson)
        (context as? HomeActivity)?.getTeamsIdlingResource?.increment()
        presenter.getLeagueList()

        spinnerAdapter = ArrayAdapter(requireContext(), simple_spinner_dropdown_item, leagueList)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val leagueData = spinner.selectedItem as LeagueData
                leagueId = leagueData.leagueId.toString()
                (context as? HomeActivity)?.getTeamsIdlingResource?.increment()
                presenter.getTeamList(leagueId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefresh.onRefresh {
            (context as? HomeActivity)?.getTeamsIdlingResource?.increment()
            presenter.getTeamList(leagueId)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLeagueList(leagueList: List<LeagueData>) {
        this.leagueList.clear()
        this.leagueList.addAll(leagueList)
        spinnerAdapter.notifyDataSetChanged()
        (context as? HomeActivity)?.getTeamsIdlingResource?.decrement()
    }

    override fun showTeamList(teamList: List<TeamData>) {
        swipeRefresh.isRefreshing = false
        this.teamList.clear()
        this.teamList.addAll(teamList)
        rvTeamsAdapter.notifyDataSetChanged()
        (context as? HomeActivity)?.getTeamsIdlingResource?.decrement()
    }
}