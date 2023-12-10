package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Period(
    @SerializedName("close")
    val close: Close?,
    @SerializedName("open")
    val `open`: Open?
)