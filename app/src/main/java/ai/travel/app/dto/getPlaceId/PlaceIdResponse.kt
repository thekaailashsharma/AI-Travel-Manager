package ai.travel.app.dto.getPlaceId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceIdResponse(
    @SerializedName("places")
    val places: List<Place?>?
)