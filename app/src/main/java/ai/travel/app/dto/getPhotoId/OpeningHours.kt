package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean?,
    @SerializedName("periods")
    val periods: List<PeriodX>?,
    @SerializedName("weekday_text")
    val weekdayText: List<String>?
)