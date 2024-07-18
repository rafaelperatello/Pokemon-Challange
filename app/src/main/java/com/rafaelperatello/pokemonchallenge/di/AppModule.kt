package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.MainViewModel
import com.rafaelperatello.pokemonchallenge.data.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.data.remote.Api
import com.rafaelperatello.pokemonchallenge.data.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

internal val appModule = module {

    single { provideHttpClient() }

    single { provideRetrofit(get()) }

    single { provideService(get()) }

    singleOf(::PokemonRepositoryImpl) { bind<PokemonRepository>() }

    viewModelOf(::MainViewModel)
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
        .addConverterFactory(networkJson.asConverterFactory("application/json; charset=UTF8".toMediaType())) // should add it at last
        .build()
}

private fun provideService(retrofit: Retrofit): PokemonApi =
    retrofit.create(PokemonApi::class.java)