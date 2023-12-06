package ai.travel.app.dto.geocoding


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodes(
    @SerializedName("items")
    val items: List<Item?>?
)