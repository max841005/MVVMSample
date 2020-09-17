package com.max.mvvmsample.data.time

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeProvider {

    fun getDelayTime(
        startTime: Long
    ) = (System.currentTimeMillis() - startTime).let {

        when {

            it < 3000L -> 3000L - it

            else -> 0L
        }
    }

    fun getAvailableSelectDate(
        currentDate: String,
        startYear: Int
    ) = CalendarConstraints.Builder().apply {

        setOpenAt(parseStringToMillis(currentDate))

        setValidator(CompositeDateValidator.allOf(listOf(
            DateValidatorPointForward.from(Calendar.getInstance().apply { set(startYear, 0, 1, 0, 0, 0) }.timeInMillis),
            DateValidatorPointBackward.now()
        )))
    }.build()

    fun getCurrentDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

    fun getCurrentTime(): String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)

    fun getPreviousDateString(
        currentDate: String
    ) : String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).let { format ->

        format.format(Calendar.getInstance().apply {

            time = try {
                format.parse(currentDate) ?: Date()
            }
            catch (e: ParseException) {

                e.printStackTrace()

                Date()
            }

            add(Calendar.DATE, -1)

        }.time)
    }

    fun getNextDateString(
        currentDate: String
    ) : String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).let { format ->

        format.format(Calendar.getInstance().apply {

            time = try {
                format.parse(currentDate) ?: Date()
            }
            catch (e: ParseException) {

                e.printStackTrace()

                Date()
            }

            add(Calendar.DATE, 1)

            if (after(Calendar.getInstance())) {
                time = Date()
            }
        }.time)
    }

    fun formatMillisToString(
        time: Long
    ): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().apply { timeInMillis = time }.time)

    fun parseStringToMillis(
        time: String
    ) = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).run {

        try {
            parse(time)?.time?.plus(86400000L) ?: Date().time
        }
        catch (e: ParseException) {

            e.printStackTrace()

            Date().time
        }
    }
}