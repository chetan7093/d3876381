package uk.ac.tees.mad.travelplanner.data

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.travelplanner.data.local.TripDao
import uk.ac.tees.mad.travelplanner.data.local.TripEntity
import uk.ac.tees.mad.travelplanner.viewmodels.Trip
import java.io.ByteArrayOutputStream
import java.util.UUID

class TripRepository(
    private val tripDao: TripDao,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val currentUser = auth.currentUser!!
    private val tripsCollection =
        firestore.collection("users").document(currentUser.uid).collection("trips")

    suspend fun createTrip(
        startLocation: String,
        destination: String,
        startDate: Long,
        endDate: Long,
        itinerary: String,
        photos: List<Bitmap>
    ): String {
        val photoUrls = uploadPhotos(photos)
        val trip = TripEntity(
            startLocation = startLocation,
            destination = destination,
            startDate = startDate,
            endDate = endDate,
            itinerary = itinerary,
            photoUrls = photoUrls
        )
        tripDao.insertTrip(trip)
        syncTripToFirestore(trip)
        return trip.id
    }

    private suspend fun uploadPhotos(photos: List<Bitmap>): List<String> {
        return photos.mapIndexed { index, bitmap ->
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val photoRef = storage.reference.child("trip_photos/${UUID.randomUUID()}.jpg")
            photoRef.putBytes(data).await()
            photoRef.downloadUrl.await().toString()
        }
    }

    private suspend fun syncTripToFirestore(trip: TripEntity) {
        try {
            tripsCollection.document(trip.id).set(trip).await()
            tripDao.updateTripSyncStatus(trip.id, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips().map { entities ->
        entities.map { it.toTrip() }
    }

    fun getTripById(id: String): Flow<Trip?> = tripDao.getTripById(id).map { it?.toTrip() }


    suspend fun syncUnSyncedTrips() {
        if (isNetworkAvailable()) {
            val unSyncedTrips = tripDao.getUnsyncedTrips()
            unSyncedTrips.forEach { trip ->
                syncTripToFirestore(trip)
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {

        return true
    }
}