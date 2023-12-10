package ai.travel.app.dto.hereSearch


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Title(
    @SerializedName("end")
    val end: Int?,
    @SerializedName("start")
    val start: Int?
)