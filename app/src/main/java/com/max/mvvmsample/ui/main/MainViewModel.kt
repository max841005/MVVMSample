package com.max.mvvmsample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import com.google.android.material.datepicker.MaterialDatePicker
import com.max.mvvmsample.R
import com.max.mvvmsample.data.entities.MainEntity
import com.max.mvvmsample.data.repositories.MainRepository
import com.max.mvvmsample.data.repositories.TimeRepository
import com.max.mvvmsample.data.repositories.WelcomeRepository

class MainViewModel(
    private val timeRepository: TimeRepository,
    private val welcomeRepository: WelcomeRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    val currentDate = MutableLiveData(timeRepository.getCurrentDate())
    val entity = MainEntity(currentDate)

    //set nowFragment
    /**
     * @param destination now destination
     */
    fun setNowFragment(
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
    //Set date
    fun getDatePicker() = MaterialDatePicker.Builder.datePicker().apply {

        setTitleText("Please Select Date")

        //Set start date
        setSelection(getStartDateInMillis())

        //Set available date
        setCalendarConstraints(getAvailableSelectDate())
    }.build().apply { addOnPositiveButtonClickListener { selection -> setDate(selection) } }
    fun setCurrentDate() = currentDate.run { value = timeRepository.getCurrentDate()  }
    fun setPreviousDate() = currentDate.run { value = value?.let { timeRepository.getPreviousDateString(it) } }
    fun setNextDate() = currentDate.run { value = value?.let { timeRepository.getNextDateString(it) } }
    private fun getAvailableSelectDate() = currentDate.value?.let { timeRepository.getAvailableSelectDate(it, 2020) }
    private fun getStartDateInMillis() = currentDate.value?.let { timeRepository.parseStringToMillis(it) }
    private fun setDate(
        timeInMillis: Long
    ) = currentDate.run { value = timeRepository.formatMillisToString(timeInMillis) }
}
