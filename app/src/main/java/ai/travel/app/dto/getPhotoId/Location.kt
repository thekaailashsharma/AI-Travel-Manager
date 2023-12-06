package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lng")
    val lng: Double?
)