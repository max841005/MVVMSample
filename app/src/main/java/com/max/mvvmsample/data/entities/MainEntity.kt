package com.max.mvvmsample.data.entities

import androidx.lifecycle.MutableLiveData

data class MainEntity(

    var currentDate: MutableLiveData<String>,

    val nowFragment: MutableLiveData<Int> = MutableLiveData(0),
    val title: MutableLiveData<CharSequence> = MutableLiveData("")
)