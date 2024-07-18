package com.rafaelperatello.pokemonchallenge.di

import android.content.Context
import com.rafaelperatello.pokemonchallenge.data.remote.Api
import com.rafaelperatello.pokemonchallenge.data.remote.PokemonApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

internal val networkModule = module {

    single { provideHttpClient(androidContext()) }

    single { provideRetrofit(get()) }

    single { provideService(get()) }
}

private fun provideHttpClient(context: Context): OkHttpClient {
    val cacheSize = 20 * 1024 * 1024
    val cacheDirectory = File(context.cacheDir, "http-cache")
    val cache = Cache(cacheDirectory, cacheSize.toLong())

    val networkInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        // Bypass cache control headers
        val cacheControl = "public, max-age=3600"

        response.newBuilder()
            .header("Cache-Control", cacheControl)
            .build()
    }

    return OkHttpClient
        .Builder()
        .cache(cache)
        .addNetworkInterceptor(networkInterceptor)
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

private fun provideService(retrofit: Retrofit): PokemonApi {
    return retrofit.create(PokemonApi::class.java)
}