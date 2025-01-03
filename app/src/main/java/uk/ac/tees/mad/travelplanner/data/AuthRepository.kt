package uk.ac.tees.mad.travelplanner.data

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.travelplanner.viewmodels.TPUser
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    val currentUser = firebaseAuth.currentUser!!
    suspend fun signUp(name: String, email: String, password: String): Result<Unit> = try {
        val userMap = mapOf(
            "name" to name,
            "email" to email
        )
        val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        user?.user?.uid?.let {
            firestore.collection("users").document(it).set(userMap).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signIn(email: String, password: String): Result<Unit> = try {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun getCurrentUser(): Result<TPUser> = try {
        val result = firestore.collection("users").document(currentUser.uid).get().await().data
        val user = TPUser(
            name = result?.get("name") as String?,
            profileUrl = result?.get("image") as String?,
            email = result?.get("email") as String?
        )
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun uploadPhoto(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val photoRef = storage.reference.child("trip_photos/${UUID.randomUUID()}.jpg")
        photoRef.putBytes(data).await()
        return photoRef.downloadUrl.await().toString()

    }

    suspend fun updateCurrentUser(username: String, imageBitmap: Bitmap? = null): Result<Unit> =
        try {
            val uploadedPhotoUrl = uploadPhoto(imageBitmap)

            firestore.collection("users").document(currentUser.uid).update(
                mapOf(
                    "image" to uploadedPhotoUrl,
                    "name" to username
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}