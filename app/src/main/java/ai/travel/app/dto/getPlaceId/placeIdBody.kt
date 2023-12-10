package ai.travel.app.dto.getPlaceId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceIdBody(
    @SerializedName("textQuery")
    val textQuery: String?
)