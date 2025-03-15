package com.example.pokedex_hacksprint_2025.common.data.remote

import com.example.pokedex_hacksprint_2025.BuildConfig
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
private const val OPENAI_API_KEY = BuildConfig.API_KEY

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