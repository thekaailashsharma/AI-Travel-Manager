package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Close(
    @SerializedName("date")
    val date: String?,
    @SerializedName("day")
    val day: Int?,
    @SerializedName("time")
    val time: String?
)