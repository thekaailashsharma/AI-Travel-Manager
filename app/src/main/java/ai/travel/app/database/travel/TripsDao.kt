package ai.travel.app.database.travel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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

    @Query("SELECT * FROM trips WHERE day = :day ORDER BY timeOfDay ASC")
    suspend fun getTripsForDay(day: String): List<TripsEntity>

    @Query("SELECT departureDate FROM trips WHERE day = :day and destination = :destination")
    fun getDepartureDate(day: String, destination: String): Flow<List<String?>>


    @Query("SELECT arrivalDate FROM trips WHERE day = :day and destination = :destination")
    fun getArrivalDate(day: String, destination: String): Flow<List<String?>>

    @Transaction
    suspend fun swapTripPositions(day: String, fromIndex: Int, toIndex: Int, destination: String) {
        val trips = getTripsForDay(day).toMutableList()
        if (fromIndex >= trips.size || toIndex >= trips.size || fromIndex == toIndex) {
            // Invalid indices or no change needed
            return
        }

        val temp = trips[fromIndex]
        trips[fromIndex] = trips[toIndex]
        trips[toIndex] = temp

//        updateTrips(trips[toIndex].name, trips[toIndex].budget, trips[toIndex].latitude, trips[toIndex].longitude,
//            trips[toIndex].photoBase64, trips[toIndex].distance, trips[toIndex].duration,
//            trips[fromIndex].id, trips[fromIndex].day, trips[fromIndex].destination)
//        val deletedTripId = trips[fromIndex].id
//        deleteTripById(deletedTripId, destination, day)
    }

    @Query("UPDATE trips SET name = :name," +
            "budget = :budget, latitude = :latitude, longitude = :longitude," +
            "photoBase64 = :photoBase64, distance = :distance," +
            "duration = :duration, timeOfDay = :timeOfDay WHERE id = :fromId and day = :fromDay and destination = :fromDestination")
    suspend fun updateTrips(name: String, budget: String?, latitude: Double?, longitude: Double?,
                            photoBase64: String?, distance: String, duration: String, timeOfDay: String,
                            fromId: Long, fromDay: String, fromDestination: String)

    @Query("DELETE FROM trips WHERE id = :tripId and destination = :destination and day = :day")
    suspend fun deleteTripById(tripId: Long, destination: String, day: String)

    @Query("Select budget from trips where destination = :destination")
    fun getBudget(destination: String): Flow<List<String?>>

    @Query("Select totalBudget from trips where destination = :destination")
    fun getTotalBudget(destination: String): Flow<List<Double?>>

}