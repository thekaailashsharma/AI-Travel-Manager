package ai.travel.app.database.travel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val day: String,
    val timeOfDay: String,
    val name: String,
    val budget: String? = null,
    val latitude: Double?,
    val longitude: Double?,
    val photoBase64: String? = null // Store photo as Base64 String
)