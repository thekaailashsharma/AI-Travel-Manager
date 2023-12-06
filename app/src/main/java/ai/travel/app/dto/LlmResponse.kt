package ai.travel.app.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PalmApi(
    @SerializedName("candidates")
    val candidates: List<Candidate?>?
)