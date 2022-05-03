@file:Suppress("unused")

package com.max.mvvmsample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import com.max.mvvmsample.R
import com.max.mvvmsample.data.config.DATE_INTERVAL_DAY
import com.max.mvvmsample.data.config.DATE_INTERVAL_MONTH
import com.max.mvvmsample.data.config.DATE_INTERVAL_SEASON
import com.max.mvvmsample.data.config.DATE_INTERVAL_WEEK
import com.max.mvvmsample.data.entities.MainEntity
import com.max.mvvmsample.data.repositories.MainRepository
import com.max.mvvmsample.utils.*
import java.time.LocalDate
import java.time.YearMonth

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    val entity = MainEntity()
    val currentDate = MutableLiveData(LocalDate.now())
    private var dateInterval = DATE_INTERVAL_DAY

    //set nowFragment
    /**
     * @param destination now destination
     */
    fun destinationChange(
        destination: NavDestination
    ) = when (destination.id) {

        R.id.list_fragment -> {

            entity.nowFragment.value = 1

            entity.title.value = ""
        }

        else -> {

            entity.nowFragment.value = 0

            entity.title.value = destination.label
        }

    }

    //TODO Set DatePicker Title
    fun setPreviousDate() = when (dateInterval) {
        DATE_INTERVAL_WEEK -> currentDate.value = currentDate.value!!.minusWeeks(1L)
        DATE_INTERVAL_MONTH -> currentDate.value = currentDate.value!!.minusMonths(1L)
        DATE_INTERVAL_SEASON -> currentDate.value = currentDate.value!!.minusMonths(3L)
        else -> currentDate.value = currentDate.value!!.minusDays(1L)
    }
    fun setNextDate() = when (dateInterval) {
        DATE_INTERVAL_WEEK -> currentDate.value = currentDate.value!!.plusWeeks(1L)
        DATE_INTERVAL_MONTH -> currentDate.value = currentDate.value!!.plusMonths(1L)
        DATE_INTERVAL_SEASON -> currentDate.value = currentDate.value!!.plusMonths(3L)
        else -> currentDate.value = currentDate.value!!.plusDays(1L)
    }

    fun setDateString() = when (dateInterval) {

        DATE_INTERVAL_WEEK -> with(entity) {
            nextDateIsDisable.value = currentDate.value!!.isNowWeek()
            dateString.value = currentDate.value!!.toWeekString()
        }

        DATE_INTERVAL_MONTH -> YearMonth.from(currentDate.value).let {

            with(entity) {
                nextDateIsDisable.value = it == YearMonth.now()
                dateString.value = it.toMonthString()
            }
        }

        DATE_INTERVAL_SEASON -> with(entity) {
            nextDateIsDisable.value = currentDate.value!!.isNowSeason()
            dateString.value = currentDate.value!!.toSeasonString()
        }

        else -> with(entity) {
            nextDateIsDisable.value = currentDate.value!! == LocalDate.now()
            dateString.value = currentDate.value!!.toWeekDayEndString()
        }
    }

    //BLE
    fun isBluetoothEnabled() = mainRepository.isBluetoothEnabled()
}
