package com.remedyal.footballapp.util

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun toGMTDate(date: String?, time: String?): Date? {
    val localId = Locale("in")
    val formatter = SimpleDateFormat("dd/MM/yy HH:mm:ss", localId)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    val dateTime = "$date $time"
    return formatter.parse(dateTime)
}

fun Date?.toDateText(): String {
    val localId = Locale("in")
    val newFormat = SimpleDateFormat("EEE, dd MMM yyyy", localId)
    return newFormat.format(this)
}

fun Date?.toTimeText(): String {
    val localId = Locale("in")
    val newFormat = SimpleDateFormat("HH:mm", localId)
    return newFormat.format(this)
}

fun String?.toPlayerStatText():String{
    return if(this.isNullOrEmpty()){
        "-"
    } else {
        when {
            this.length > 5 -> this.substring(0, 5)
            this.length > 4 -> this.substring(0, 4)
            else -> this
        }
    }
}

fun String?.toGoalDetailsText(): String {
    return this?.replace(";", ";\n")?.replace(":", " : ")?.replace("(", "\n(") ?: ""
}

fun String?.toLineupText(): String {
    return this?.replace("; ", ";\n") ?: ""
}

fun Int?.toText(): String {
    return this?.toString() ?: ""
}