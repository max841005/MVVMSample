package com.max.mvvmsample.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "db_data"
)
data class DBData(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var data: String? = null
)