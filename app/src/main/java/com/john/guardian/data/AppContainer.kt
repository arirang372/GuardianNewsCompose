package com.john.guardian.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface AppContainer {
    val repository: GuardianNewsRepository
}

class AppDataContainer : AppContainer {
    override val repository: GuardianNewsRepository by lazy {
        GuardianNewsRepository(retrofitService)
    }

    private val logger = HttpLoggingInterceptor { Log.d("API", it) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: GuardianNewsService by lazy {
        retrofit.create(GuardianNewsService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://content.guardianapis.com"
    }
}