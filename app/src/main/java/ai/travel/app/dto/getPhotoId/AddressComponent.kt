package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AddressComponent(
    @SerializedName("long_name")
    val longName: String?,
    @SerializedName("short_name")
    val shortName: String?,
    @SerializedName("types")
    val types: List<String?>?
)