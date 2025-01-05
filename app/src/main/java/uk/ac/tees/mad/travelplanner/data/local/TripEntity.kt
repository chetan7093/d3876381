package uk.ac.tees.mad.travelplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.travelplanner.viewmodels.Trip
import java.util.UUID

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val startLocation: String,
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val itinerary: String,
    val photoData: List<ByteArray> = emptyList(), // List of image byte arrays
    val isSynced: Boolean = false,
    val userId: String
)

fun TripEntity.toTrip() = Trip(
    id = this.id,
    startLocation = this.startLocation,
    destination = this.destination,
    startDate = this.startDate,
    endDate = this.endDate,
    itinerary = this.itinerary,
    photoData = this.photoData
)



