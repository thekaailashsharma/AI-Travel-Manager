package ai.travel.app.dto.distanceMatrix


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class DistanceMatrixResponse(
    @SerializedName("destination_addresses")
    val destinationAddresses: List<String?>?,
    @SerializedName("origin_addresses")
    val originAddresses: List<String?>?,
    @SerializedName("rows")
    val rows: List<Row?>?,
    @SerializedName("status")
    val status: String?
)