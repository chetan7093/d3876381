package uk.ac.tees.mad.travelplanner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips WHERE userId=:userId")
    fun getAllTrips(userId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTripById(id: String): Flow<TripEntity?>

    @Upsert
    suspend fun insertTrip(trip: TripEntity)

    @Query("UPDATE trips SET isSynced = :isSynced WHERE id = :id")
    suspend fun updateTripSyncStatus(id: String, isSynced: Boolean)

    @Query("SELECT * FROM trips WHERE isSynced = 0 and userId=:userId")
    suspend fun getUnsyncedTrips(userId: String): List<TripEntity>
}