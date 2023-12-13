package ai.travel.app.dto.distanceMatrix


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Element(
    @SerializedName("distance")
    val distance: Distance?,
    @SerializedName("duration")
    val duration: Duration?,
    @SerializedName("status")
    val status: String?
)