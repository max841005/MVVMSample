package com.max.mvvmsample.data.entities

import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

data class MainEntity(

    val dateString: MutableLiveData<String> = MutableLiveData(LocalDate.now().toString()),
    val nextDateIsDisable: MutableLiveData<Boolean> = MutableLiveData(false),

    val nowFragment: MutableLiveData<Int> = MutableLiveData(0),
    val title: MutableLiveData<CharSequence> = MutableLiveData("")
)