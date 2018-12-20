package com.remedyal.footballapp.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerData(
    @SerializedName("idPlayer")
    var playerId: String? = null,

    @SerializedName("strPlayer")
    var playerName: String? = null,

    @SerializedName("strHeight")
    var playerHeight: String? = null,

    @SerializedName("strWeight")
    var playerWeight: String? = null,

    @SerializedName("strPosition")
    var playerPosition: String? = null,

    @SerializedName("strCutout")
    var playerCutout: String? = null,

    @SerializedName("strFanart1")
    var playerFanart: String? = null,

    @SerializedName("strDescriptionEN")
    var playerDescription: String? = null
) : Parcelable