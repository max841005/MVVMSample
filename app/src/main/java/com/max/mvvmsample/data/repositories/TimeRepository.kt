package com.max.mvvmsample.data.repositories

import com.max.mvvmsample.data.time.TimeProvider

class TimeRepository(
    private val time: TimeProvider
) {

    fun getDelayTime(startTime: Long) = time.getDelayTime(startTime)

    fun getAvailableSelectDate(currentDate: String, startYear: Int) = time.getAvailableSelectDate(currentDate, startYear)

    fun getCurrentDate() = time.getCurrentDate()

    fun getPreviousDateString(currentDate: String) = time.getPreviousDateString(currentDate)

    fun getNextDateString(currentDate: String) = time.getNextDateString(currentDate)

    fun formatMillisToString(timeInMillis: Long) = time.formatMillisToString(timeInMillis)

    fun parseStringToMillis(time: String) = this.time.parseStringToMillis(time)
}