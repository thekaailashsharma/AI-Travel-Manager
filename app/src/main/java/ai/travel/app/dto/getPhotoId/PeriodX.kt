package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PeriodX(
    @SerializedName("close")
    val close: CloseX?,
    @SerializedName("open")
    val `open`: OpenX?
)