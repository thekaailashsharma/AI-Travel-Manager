package ai.travel.app.repository

import ai.travel.app.BuildConfig
import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.Candidate
import ai.travel.app.dto.PalmApi
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI

class ApiServiceImpl(
    private val client: HttpClient,
) : ApiService {

    override suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi {
        return try {
            client.post {
                url("${ApiRoutes.BASE_URL}?key=${BuildConfig.API_KEY}")
                setBody(apiPrompt)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }.body<PalmApi>()
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return PalmApi(
                candidates = listOf(
                    Candidate(
                        output = e.message.toString(),
                        safetyRatings = null
                    )
                )
            )
        }

    }
}

