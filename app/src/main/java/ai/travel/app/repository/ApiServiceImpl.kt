package ai.travel.app.repository

import ai.travel.app.BuildConfig
import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.Candidate
import ai.travel.app.dto.PalmApi
import ai.travel.app.dto.geocoding.GeoCodes
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI
import java.net.URLEncoder

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

    override suspend fun getGeocodingData(query: String): GeoCodes {
        return try {
            client.get {
            val encodedLocation = URLEncoder.encode(query, "UTF-8")
            url("${ApiRoutes.Geocoding_URL}?q=$encodedLocation&apiKey=${BuildConfig.Here_API_KEY}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            headers {
                append("Accept", "*/*")
                append("Content-Type", "application/json")
            }
        }.body()
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return GeoCodes(
                items = null
            )
        }
    }
}

