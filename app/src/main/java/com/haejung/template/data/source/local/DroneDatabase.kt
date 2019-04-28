package com.haejung.template.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haejung.template.data.Drone

@Database(entities = [Drone::class], exportSchema = false, version = 1)
abstract class DroneDatabase : RoomDatabase() {

    abstract fun droneDao(): DroneDao

    companion object {
        private var instance: DroneDatabase? = null

        fun getInstance(context: Context): DroneDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DroneDatabase::class.java,
                    "Drones.db"
                ).build().apply {
                    instance = this
                }
            }
        }
    }

}
