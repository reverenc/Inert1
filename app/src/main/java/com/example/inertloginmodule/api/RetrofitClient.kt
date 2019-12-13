package com.example.inertloginmodule.api

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private val AUTH = "Basic "+ Base64.encodeToString("Jagruti".toByteArray(), Base64.NO_WRAP)

    private const val BASE_URL = "http://192.168.0.4:8080/Inert/"

    private val logger: okhttp3.logging.HttpLoggingInterceptor
        get() {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            return logging
        }


    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->

                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        .addHeader("Authorization", AUTH)
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()


    val instance: Api by lazy{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        retrofit.create(Api::class.java)
    }

}