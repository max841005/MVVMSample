@file:Suppress("unused")

package com.max.mvvmsample.view

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("data")
fun setDiabetesType(
    view: View,
    data: Int
) = view.apply {


}

@BindingAdapter(value = ["data1", "data2"], requireAll = true)
fun setTime(
    view: View,
    data1: Int,
    data2: Boolean
) = view.apply {


}