package com.fpinbo.app.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit.Builder
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://fpinbo.dev/data/"
    }

    @Provides
    @Singleton
    fun provideApi(): Api {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            prettyPrint = true
        }

        val retrofit = Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(EitherAdapterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(Api::class.java)
    }
}
