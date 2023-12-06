package ai.travel.app.dto.getPlaceId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerializedName("displayName")
    val displayName: DisplayName?,
    @SerializedName("formattedAddress")
    val formattedAddress: String?,
    @SerializedName("id")
    val id: String?
)