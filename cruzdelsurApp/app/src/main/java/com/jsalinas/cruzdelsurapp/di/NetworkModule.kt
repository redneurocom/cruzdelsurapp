package com.jsalinas.cruzdelsurapp.di

import com.jsalinas.cruzdelsurapp.data.remote.api.DestinoApiService
import com.jsalinas.cruzdelsurapp.data.remote.api.EquipajeApiService
import com.jsalinas.cruzdelsurapp.data.repository.EquipajeRepositoryImpl
import com.jsalinas.cruzdelsurapp.domain.repository.EquipajeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val EQUIPAJES_BASE_URL = "https://wwwbnjtcae.execute-api.us-east-1.amazonaws.com/"
    private const val DESTINOS_BASE_URL = "https://op1xpdid16.execute-api.us-east-1.amazonaws.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    }

    // --- Retrofit para Equipajes ---
    @Provides
    @Singleton
    @EquipajesRetrofit
    fun provideEquipajesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(EQUIPAJES_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideEquipajeApiService(@EquipajesRetrofit retrofit: Retrofit): EquipajeApiService {
        return retrofit.create(EquipajeApiService::class.java)
    }

    // --- Retrofit para Destinos ---
    @Provides
    @Singleton
    @DestinosRetrofit
    fun provideDestinosRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DESTINOS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDestinoApiService(@DestinosRetrofit retrofit: Retrofit): DestinoApiService {
        return retrofit.create(DestinoApiService::class.java)
    }

    // --- Repositorio ---
    @Provides
    @Singleton
    fun provideEquipajeRepository(
        equipajeApi: EquipajeApiService,
        destinoApi: DestinoApiService
    ): EquipajeRepository {
        return EquipajeRepositoryImpl(equipajeApi, destinoApi)
    }
}
