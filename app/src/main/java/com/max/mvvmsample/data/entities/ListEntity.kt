package com.max.mvvmsample.data.entities

import androidx.lifecycle.MutableLiveData

data class ListEntity(

    val hasData: MutableLiveData<Boolean> = MutableLiveData(false)
)