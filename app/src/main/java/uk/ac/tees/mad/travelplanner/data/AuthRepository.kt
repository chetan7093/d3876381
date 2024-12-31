package uk.ac.tees.mad.travelplanner.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
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

    fun getCurrentUser() = firebaseAuth.currentUser
}