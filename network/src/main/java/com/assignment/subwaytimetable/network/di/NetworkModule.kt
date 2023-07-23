package com.assignment.subwaytimetable.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideKtorHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                json = Json(from = DefaultJson) {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    L.i(message)
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 15.seconds.inWholeMilliseconds
            requestTimeoutMillis = 60.seconds.inWholeMilliseconds
            socketTimeoutMillis = 60.seconds.inWholeMilliseconds
        }
    }
}

typealias L = com.orhanobut.logger.Logger