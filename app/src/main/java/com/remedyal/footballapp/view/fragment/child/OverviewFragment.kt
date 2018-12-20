package com.remedyal.footballapp.view.fragment.child

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.nestedScrollView

class OverviewFragment : Fragment(), AnkoComponent<Context> {

    companion object {
        fun newInstance(teamOverview: String): OverviewFragment {
            val fragment = OverviewFragment()
            val args = Bundle()
            args.putString("teamOverview", teamOverview)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var teamOverview: String
    private lateinit var tvTeamOverview: TextView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        teamOverview = arguments?.getString("teamOverview").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        nestedScrollView {
            lparams(width = matchParent, height = wrapContent)
            padding = dip(16)

            tvTeamOverview = textView(teamOverview) {
                isVerticalScrollBarEnabled = true
            }
        }
    }
}