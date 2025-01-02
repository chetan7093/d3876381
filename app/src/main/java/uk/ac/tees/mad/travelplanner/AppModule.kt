package uk.ac.tees.mad.travelplanner

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.travelplanner.data.local.AppDatabase
import uk.ac.tees.mad.travelplanner.data.local.TripDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "trip_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTripDao(database: AppDatabase): TripDao {
        return database.tripDao()
    }

}