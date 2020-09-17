package com.max.mvvmsample.data.entities

import androidx.lifecycle.MutableLiveData

data class HomeEntity(

    val data: MutableLiveData<String> = MutableLiveData("")
)