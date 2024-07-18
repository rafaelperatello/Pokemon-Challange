package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.data.remote.Api
import com.rafaelperatello.pokemonchallenge.data.remote.PokemonApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

internal val networkModule = module {

    single { provideHttpClient() }

    single { provideRetrofit(get()) }

    single { provideService(get()) }
}

private fun provideHttpClient(): OkHttpClient {
    return OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
): Retrofit {
    val networkJson = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(networkJson.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()
}

private fun provideService(retrofit: Retrofit): PokemonApi =
    retrofit.create(PokemonApi::class.java)