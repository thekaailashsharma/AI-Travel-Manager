package ai.travel.app.dto.hereSearch


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerializedName("access")
    val access: List<Acces>?,
    @SerializedName("address")
    val address: Address?,
    @SerializedName("categories")
    val categories: List<Category>?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("highlights")
    val highlights: Highlights?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("localityType")
    val localityType: String?,
    @SerializedName("mapView")
    val mapView: MapView?,
    @SerializedName("position")
    val position: Position?,
    @SerializedName("references")
    val references: List<Reference>?,
    @SerializedName("resultType")
    val resultType: String?,
    @SerializedName("title")
    val title: String?
)