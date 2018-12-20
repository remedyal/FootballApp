package com.remedyal.footballapp.view.fragment.child

import com.remedyal.footballapp.view.fragment.child.base.BaseEventsFragment

class PastEventsFragment : BaseEventsFragment() {
    override fun getEventList() {
        presenter.getPastEventList(leagueId)
    }
}
