package com.grupo6.cruzdelsurapp.di

import com.grupo6.cruzdelsurapp.data.remote.api.AuthApiService
import com.grupo6.cruzdelsurapp.data.remote.api.DestinoApiService
import com.grupo6.cruzdelsurapp.data.remote.api.EquipajeApiService
import com.grupo6.cruzdelsurapp.data.repository.AuthRepositoryImpl
import com.grupo6.cruzdelsurapp.data.repository.EquipajeRepositoryImpl
import com.grupo6.cruzdelsurapp.domain.repository.AuthRepository
import com.grupo6.cruzdelsurapp.domain.repository.EquipajeRepository
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
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
    private const val EQUIPAJES_BASE_URL = "https://7bs0xq8em0.execute-api.us-east-1.amazonaws.com/"
    private const val DESTINOS_BASE_URL = "https://de6k6bqa47.execute-api.us-east-1.amazonaws.com/"
    private const val AUTH_BASE_URL = "https://kcmfwaje63.execute-api.us-east-1.amazonaws.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(sesionManager: SesionManager): OkHttpClient {
        return OkHttpClient.Builder()
            // Envía el token del operario (si hay sesión) para registrar, editar y eliminar
            .addInterceptor { chain ->
                val token = sesionManager.token
                val request = if (token.isNullOrBlank()) chain.request()
                else chain.request().newBuilder().header("Authorization", "Bearer $token").build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
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

    // --- Retrofit para Autenticación ---
    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(@AuthRetrofit retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    // --- Repositorios ---
    @Provides
    @Singleton
    fun provideEquipajeRepository(
        equipajeApi: EquipajeApiService,
        destinoApi: DestinoApiService
    ): EquipajeRepository {
        return EquipajeRepositoryImpl(equipajeApi, destinoApi)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApiService): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }
}