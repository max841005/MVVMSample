package com.max.mvvmsample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.max.mvvmsample.data.db.entities.DBData

@Database(
    entities = [DBData::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDBDataDao(): DBDataDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        //TODO Set DB Name
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "MyDatabase.db"
        ).build()
    }
}