package ai.travel.app.repository

import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.PalmApi
import ai.travel.app.dto.geocoding.GeoCodes


interface ApiService {
    suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi

    suspend fun getGeocodingData(query: String): GeoCodes
}