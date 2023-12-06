package ai.travel.app.dto.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerializedName("address_components")
    val addressComponents: List<AddressComponent>?,
    @SerializedName("adr_address")
    val adrAddress: String?,
    @SerializedName("business_status")
    val businessStatus: String?,
    @SerializedName("current_opening_hours")
    val currentOpeningHours: CurrentOpeningHours?,
    @SerializedName("editorial_summary")
    val editorialSummary: EditorialSummary?,
    @SerializedName("formatted_address")
    val formattedAddress: String?,
    @SerializedName("formatted_phone_number")
    val formattedPhoneNumber: String?,
    @SerializedName("geometry")
    val geometry: Geometry?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String?,
    @SerializedName("icon_mask_base_uri")
    val iconMaskBaseUri: String?,
    @SerializedName("international_phone_number")
    val internationalPhoneNumber: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("opening_hours")
    val openingHours: OpeningHours?,
    @SerializedName("photos")
    val photos: List<Photo>?,
    @SerializedName("place_id")
    val placeId: String?,
    @SerializedName("plus_code")
    val plusCode: PlusCode?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("reference")
    val reference: String?,
    @SerializedName("reviews")
    val reviews: List<Review>?,
    @SerializedName("types")
    val types: List<String>?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int?,
    @SerializedName("utc_offset")
    val utcOffset: Int?,
    @SerializedName("vicinity")
    val vicinity: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("wheelchair_accessible_entrance")
    val wheelchairAccessibleEntrance: Boolean?
)