package ai.travel.app.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Prompt(
    @SerializedName("text")
    val text: String?
)