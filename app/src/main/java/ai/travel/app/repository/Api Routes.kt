package ai.travel.app.repository

object ApiRoutes {
    const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta2/" +
            "models/text-bison-001:generateText"

    const val Geocoding_URL = "https://geocode.search.hereapi.com/v1/geocode"

    const val getPlaceIdApi = "https://places.googleapis.com/v1/places:searchText"

    const val getPhotoIdApi = "https://maps.googleapis.com/maps/api/place/details/json"

    const val getPhoto = "https://maps.googleapis.com/maps/api/place/photo"

    const val hereSearch = "https://autosuggest.search.hereapi.com/v1/autosuggest"

    const val distanceMatrix = "https://maps.googleapis.com/maps/api/distancematrix/json"
}