package ai.travel.app.repository

import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.PalmApi


interface ApiService {
    suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi
}