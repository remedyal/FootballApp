package com.remedyal.footballapp.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueData(
    @SerializedName("idLeague")
    var leagueId: String? = null,

    @SerializedName("strLeague")
    var leagueName: String? = null,

    @SerializedName("strSport")
    var leagueSport: String? = null
) : Parcelable {

    override fun toString(): String {
        return this.leagueName.toString()
    }
}