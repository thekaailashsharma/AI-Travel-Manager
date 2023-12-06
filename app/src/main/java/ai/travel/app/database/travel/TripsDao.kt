package ai.travel.app.database.travel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TripsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(tourDetails: TripsEntity)

    @Query("SELECT * FROM trips where day = :day and timeOfDay = :timeOfDay")
    fun getTrips(day: String, timeOfDay: String): Flow<List<TripsEntity?>>

    @Query("SELECT * FROM trips")
    fun getAllTrips(): Flow<List<TripsEntity?>>

}