package com.skyrel74.ricknmorty.di.modules

import android.util.Log
import com.skyrel74.ricknmorty.data.remote.CharacterService
import com.skyrel74.ricknmorty.data.remote.EpisodeService
import com.skyrel74.ricknmorty.di.Application.Companion.API_BASE_URL
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
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

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
            Log.e("qwe", request.toString())
            Log.e("qwe", response.toString())
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
}