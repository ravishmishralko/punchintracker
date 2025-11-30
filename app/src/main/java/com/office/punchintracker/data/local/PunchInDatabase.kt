package com.office.punchintracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PunchInEntity::class], version = 1, exportSchema = false)
abstract class PunchInDatabase : RoomDatabase() {
    abstract fun punchInDao(): PunchInDao

    companion object {
        @Volatile
        private var INSTANCE: PunchInDatabase? = null

        fun getDatabase(context: Context): PunchInDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PunchInDatabase::class.java,
                    "punchin_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}