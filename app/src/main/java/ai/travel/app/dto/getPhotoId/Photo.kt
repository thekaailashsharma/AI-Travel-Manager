package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    @SerializedName("height")
    val height: Int?,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String?>?,
    @SerializedName("photo_reference")
    val photo_reference: String?,
    @SerializedName("width")
    val width: Int?
)