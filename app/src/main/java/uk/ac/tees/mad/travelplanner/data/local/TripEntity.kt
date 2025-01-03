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
    val photoUrls: List<String>,
    val isSynced: Boolean = false
) {
    fun toTrip() = Trip(
        id = this.id,
        startLocation = this.startLocation,
        destination = this.destination,
        startDate = this.startDate,
        endDate = this.endDate,
        itinerary = this.itinerary,
        photoUrl = this.photoUrls
    )
}
