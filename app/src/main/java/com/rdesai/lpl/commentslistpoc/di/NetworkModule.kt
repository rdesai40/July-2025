package com.rdesai.lpl.commentslistpoc.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rdesai.lpl.commentslistpoc.data.api.CommentsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import com.rdesai.lpl.commentslistpoc.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOKHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideCommentsApiService(retrofit: Retrofit): CommentsApiService =
        retrofit.create(CommentsApiService::class.java)

}