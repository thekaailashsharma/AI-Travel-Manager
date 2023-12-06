package ai.travel.app.di


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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                GsonSerializer(){
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
                json()
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

}