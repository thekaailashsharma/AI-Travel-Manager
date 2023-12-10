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

    @Query("SELECT * FROM trips where day = :day and destination = :destination")
    fun getTrips(day: String, destination: String): Flow<List<TripsEntity?>>

    @Query("SELECT * FROM trips")
    fun getAllTrips(): Flow<List<TripsEntity?>>

    @Query("SELECT * FROM trips where destination = :destination")
    fun getCurrentTrip(destination: String): Flow<List<TripsEntity?>>

    @Query("SELECT Distinct day FROM trips where destination = :destination")
    fun getUniqueDays(destination: String): Flow<List<String?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTrips(trips: List<TripsEntity>)

    @Query("SELECT * FROM trips where name = :destination")
    fun getMoreInfo(destination: String): Flow<List<TripsEntity?>>

}