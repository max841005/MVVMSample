package com.max.mvvmsample.data.entities.item

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListItemEntity(
    val data: String = ""
) : Parcelable