package com.remedyal.footballapp.view.fragment.child

import com.remedyal.footballapp.view.fragment.child.base.BaseEventsFragment

class NextEventsFragment : BaseEventsFragment() {
    override fun getEventList() {
        presenter.getNextEventList(leagueId)
    }
}
