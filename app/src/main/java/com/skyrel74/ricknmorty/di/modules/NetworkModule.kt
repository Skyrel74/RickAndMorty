package com.skyrel74.ricknmorty.di.modules

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skyrel74.ricknmorty.data.remote.CharacterService
import com.skyrel74.ricknmorty.data.remote.EpisodeService
import com.skyrel74.ricknmorty.data.remote.LocationService
import com.skyrel74.ricknmorty.di.Application.Companion.API_BASE_URL
import com.skyrel74.ricknmorty.util.UriDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideCallAdapterFactory(): RxJava3CallAdapterFactory = RxJava3CallAdapterFactory.create()

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder().registerTypeAdapter(Uri::class.java, UriDeserializer()).create()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideRetrofitClient(
        callAdapterFactory: RxJava3CallAdapterFactory,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(OkHttpClient().newBuilder().addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            response
        }.build())
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()

    @Singleton
    @Provides
    fun provideCharacterService(client: Retrofit): CharacterService =
        client.create(CharacterService::class.java)

    @Singleton
    @Provides
    fun provideEpisodeService(client: Retrofit): EpisodeService =
        client.create(EpisodeService::class.java)

    @Singleton
    @Provides
    fun provideLocationService(client: Retrofit): LocationService =
        client.create(LocationService::class.java)
}