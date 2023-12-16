package ai.travel.app.database.travel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val day: String,
    var timeOfDay: String,
    var name: String,
    val source: String,
    val destination: String,
    var travelActivity: String,
    var budget: String? = null,
    var latitude: Double?,
    var longitude: Double?,
    var photoBase64: String? = null, // Store photo as Base64 String,
    var distance: String = "0",
    var duration: String = "0",
    var totalBudget: Double? = 0.0,
    var departureDate: String? = null,
    var arrivalDate: String? = null,
    var departureTime: String? = null,
    var arrivalTime: String? = null,
)


sealed class TravelActivity(val activityName: String) {
    object Temples : TravelActivity("Temples")
    object Beach : TravelActivity("Beach")
    object Hiking : TravelActivity("Hiking")
    object Sightseeing : TravelActivity("Sightseeing")
    object AdventureSports : TravelActivity("Adventure Sports")
    object WildlifeSafari : TravelActivity("Wildlife Safari")
    object CulturalExperiences : TravelActivity("Cultural Experiences")
    object Shopping : TravelActivity("Shopping")
    object FoodTour : TravelActivity("Food Tour")
    object Cruise : TravelActivity("Cruise")
    object HistoricalPlaces : TravelActivity("Historical Places")
    object Photography : TravelActivity("Photography")
    object MuseumVisit : TravelActivity("Museum Visit")
    object Nightlife : TravelActivity("Nightlife")
    object SpaRelaxation : TravelActivity("Spa and Relaxation")
    object Skiing : TravelActivity("Skiing")
    object ScubaDiving : TravelActivity("Scuba Diving")
    object Parasailing : TravelActivity("Parasailing")
    object YogaMeditation : TravelActivity("Yoga and Meditation")
    object FestivalsEvents : TravelActivity("Festivals and Events")
    object LocalMarkets : TravelActivity("Exploring Local Markets")
    object Rafting : TravelActivity("Rafting")
    // Add more activities as needed
}


fun stringToTravelActivity(activityName: String): TravelActivity? {
    return TravelActivity::class.sealedSubclasses
        .mapNotNull { it.objectInstance }
        .find { it.activityName.equals(activityName, ignoreCase = true) }
}

fun travelActivityToString(activity: TravelActivity): String {
    return activity.activityName
}


