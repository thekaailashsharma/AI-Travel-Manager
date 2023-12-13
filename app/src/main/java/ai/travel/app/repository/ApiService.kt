package ai.travel.app.repository

import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.PalmApi
import ai.travel.app.dto.distanceMatrix.DistanceMatrixResponse
import ai.travel.app.dto.geocoding.GeoCodes
import ai.travel.app.dto.getPhotoId.PhotoIdResponse
import ai.travel.app.dto.getPlaceId.PlaceIdBody
import ai.travel.app.dto.getPlaceId.PlaceIdResponse
import ai.travel.app.dto.hereSearch.HereSearchResponse


interface ApiService {
    suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi

    suspend fun getGeocodingData(query: String): GeoCodes

    suspend fun getPlaceIdData(text: PlaceIdBody): PlaceIdResponse
    suspend fun getPhotoId(photoId: String): PhotoIdResponse

    suspend fun getPhoto(
        photoReference: String,
        maxWidth: Int,
        maxHeight: Int = 0,
    ): ByteArray

    suspend fun hereSearch(
        query: String,
        latitude: Double,
        longitude: Double,
        limit: Int = 6,
    ): HereSearchResponse

    suspend fun getDistanceMatrix(
        origins: String,
        destinations: String,
        units: String = "imperial",
    ): DistanceMatrixResponse
}