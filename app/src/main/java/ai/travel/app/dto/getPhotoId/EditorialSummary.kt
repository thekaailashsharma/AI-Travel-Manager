package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EditorialSummary(
    @SerializedName("language")
    val language: String?,
    @SerializedName("overview")
    val overview: String?
)