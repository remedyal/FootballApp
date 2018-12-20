package com.remedyal.footballapp.util

import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class UtilsKtTest {

    private val strDate: String = "29/12/14"
    private val strTime: String = "20:00:00+00:00"
    private val formatter = SimpleDateFormat("dd/MM/yy HH:mm:ss")
    private val validDate: Date = formatter.parse("30/12/14 03:00:00")
    private val validStrDate: String = "Sel, 30 Des 2014"
    private val validStrTime: String = "03:00"

    @Test
    fun testToGMTDate() {
        assertEquals(validDate, toGMTDate(strDate, strTime))
    }

    @Test
    fun testToDateText() {
        assertEquals(validStrDate, validDate.toDateText())
    }

    @Test
    fun testToTimeText() {
        assertEquals(validStrTime, validDate.toTimeText())
    }
}