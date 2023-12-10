package ai.travel.app.dto.hereSearch


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MapView(
    @SerializedName("east")
    val east: Double?,
    @SerializedName("north")
    val north: Double?,
    @SerializedName("south")
    val south: Double?,
    @SerializedName("west")
    val west: Double?
)