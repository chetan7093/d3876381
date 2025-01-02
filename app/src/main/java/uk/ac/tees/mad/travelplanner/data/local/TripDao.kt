package uk.ac.tees.mad.travelplanner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTripById(id: String): Flow<TripEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Query("UPDATE trips SET isSynced = :isSynced WHERE id = :id")
    suspend fun updateTripSyncStatus(id: String, isSynced: Boolean)

    @Query("SELECT * FROM trips WHERE isSynced = 0")
    suspend fun getUnsyncedTrips(): List<TripEntity>
}