package uk.ac.tees.mad.travelplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val itinerary: String,
    val photoUrls: List<String>,
    val isSynced: Boolean = false
)
