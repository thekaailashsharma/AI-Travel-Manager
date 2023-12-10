package ai.travel.app.dto.hereSearch


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Reference(
    @SerializedName("id")
    val id: String?,
    @SerializedName("supplier")
    val supplier: Supplier?
)