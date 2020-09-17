package com.max.mvvmsample.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.max.mvvmsample.data.db.entities.DBData

@Dao
interface DBDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbData: DBData)

    @Query("SELECT * FROM db_data WHERE id = :idInt")
    suspend fun getData(idInt: Int) : List<DBData>
}