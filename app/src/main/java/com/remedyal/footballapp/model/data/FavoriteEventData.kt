package com.remedyal.footballapp.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteEventData (
    val id: Long?, val eventId: String?, val eventDate: String?, val eventTime: String?,
    val homeTeamName: String?, val awayTeamName: String?, val homeTeamScore: Int?, val awayTeamScore: Int?
) : Parcelable {

    companion object {
        const val TABLE_EVENT_FAVORITE: String = "TABLE_EVENT_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_DATE: String = "EVENT_DATE"
        const val EVENT_TIME: String = "EVENT_TIME"
        const val HOME_TEAM: String = "HOME_TEAM"
        const val AWAY_TEAM: String = "AWAY_TEAM"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
    }
}