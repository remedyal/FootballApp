package com.remedyal.footballapp.view.fragment.child.base

import android.R.color
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.*
import com.remedyal.footballapp.R
import com.remedyal.footballapp.R.color.*
import com.remedyal.footballapp.adapter.recycler.EventsRecyclerAdapter
import com.remedyal.footballapp.util.invisible
import com.remedyal.footballapp.model.data.EventData
import com.remedyal.footballapp.model.data.LeagueData
import com.remedyal.footballapp.network.ApiRepository
import com.remedyal.footballapp.presenter.EventsPresenter
import com.remedyal.footballapp.view.interfaces.EventsView
import com.remedyal.footballapp.util.visible
import com.remedyal.footballapp.view.activity.EventDetailActivity
import com.remedyal.footballapp.view.activity.HomeActivity
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

abstract class BaseEventsFragment : Fragment(), AnkoComponent<Context>, EventsView {

    open var leagueId: String? = null
    private var leagueList: MutableList<LeagueData> = mutableListOf()
    lateinit var presenter: EventsPresenter
    private var eventList: MutableList<EventData> = mutableListOf()
    private lateinit var rvEvents: RecyclerView
    private lateinit var rvEventsAdapter: EventsRecyclerAdapter
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<*>
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(AnkoContext.create(requireContext()))
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                rvEventsAdapter.filter.filter(p0)
                return false
            }
        })
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(8)
            leftPadding = dip(8)
            rightPadding = dip(8)

            spinner = spinner {
                id = R.id.spinner_leagues
            }
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    colorAccent,
                    color.holo_green_light,
                    color.holo_orange_light,
                    color.holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    rvEvents = recyclerView {
                        id = R.id.rv_events
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerInParent()
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        rvEventsAdapter = EventsRecyclerAdapter(requireContext(), eventList) {
            context?.startActivity<EventDetailActivity>(
                "id" to "${it.eventId}"
            )
        }

        rvEvents.adapter = rvEventsAdapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = EventsPresenter(this, request, gson)
        (context as? HomeActivity)?.getEventsIdlingResource?.increment()
        presenter.getLeagueList()

        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, leagueList)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedLeague = spinner.selectedItem as LeagueData
                leagueId = selectedLeague.leagueId.toString()
                (context as? HomeActivity)?.getEventsIdlingResource?.increment()
                getEventList()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefresh.onRefresh {
            (context as? HomeActivity)?.getEventsIdlingResource?.increment()
            getEventList()
        }
    }

    abstract fun getEventList()

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
        (context as? HomeActivity)?.getEventsIdlingResource?.decrement()
    }

    override fun showEventList(eventList: List<EventData>) {
        swipeRefresh.isRefreshing = false
        this.eventList.clear()
        this.eventList.addAll(eventList)
        rvEvents.adapter?.notifyDataSetChanged()
        (context as? HomeActivity)?.getEventsIdlingResource?.decrement()
    }
}