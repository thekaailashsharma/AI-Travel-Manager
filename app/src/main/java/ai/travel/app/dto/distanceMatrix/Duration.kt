package ai.travel.app.dto.distanceMatrix


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Duration(
    @SerializedName("text")
    val text: String?,
    @SerializedName("value")
    val value: Int?
)