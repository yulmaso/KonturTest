package com.yulmaso.konturtest.di.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yulmaso.konturtest.data.network.Api
import com.yulmaso.konturtest.data.network.CalendarDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object NetworkModule {

    fun create() = module {
        val httpClient = provideOkHttpClient()
        val gson = provideGson()
        val retrofit = provideRetrofit(httpClient, gson)

        single(createdAtStart = true) { provideApi(retrofit) }
    }

    private fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    private fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Calendar::class.java, CalendarDeserializer())
            .create()
    }

}