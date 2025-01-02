package uk.ac.tees.mad.travelplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TripEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}