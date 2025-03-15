package com.example.pokedex_hacksprint_2025.common.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private const val OPENAI_BASE_URL = "https://api.openai.com/v1/"
private const val OPENAI_API_KEY = "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"

private val httpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header("Authorization", "Bearer $OPENAI_API_KEY")
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()
}


object RetrofitOpenAI {
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OPENAI_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}