package com.example.kotlinfacebookloginwithtoken.api.service

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyClient private constructor() {
    //   private val retrofit: Retrofit
    // val myApi: MyApi
    //     get() = retrofit.create(MyApi::class.java)

    companion object {
        private const val BASE_URL = "http://192.168.0.4:8080/Inert/"
        private val AUTH = "Basic "+ Base64.encodeToString("sana".toByteArray(), Base64.NO_WRAP)

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
        val instance: MyApi by lazy{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            retrofit.create(MyApi::class.java)
        }

        /* private var myClient: MyClient? = null
         @get:Synchronized
         val instance: MyClient?
             get() {
                 if (myClient == null) {
                     myClient = MyClient()
                 }
                 return myClient
             }*/
    }
/*
    init {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }*/
}