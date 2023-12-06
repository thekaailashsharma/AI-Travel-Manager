package ai.travel.app.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ApiPrompt(
    @SerializedName("prompt")
    val prompt: Prompt?
)