package ai.travel.app.di


import ai.travel.app.database.DatabaseObject
import ai.travel.app.database.DatabaseRepo
import ai.travel.app.repository.ApiService
import ai.travel.app.repository.ApiServiceImpl
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.gson.GsonSerializer
import io.ktor.client.plugins.json.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(Logging)
            install(WebSockets)
            install(HttpTimeout) {
                requestTimeoutMillis = 1000000
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService {
        return ApiServiceImpl(client = client)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepo(@ApplicationContext context: Context): DatabaseRepo {
        val dB = DatabaseObject.getInstance(context)
        return DatabaseRepo(dB.tripsDao())
    }

}