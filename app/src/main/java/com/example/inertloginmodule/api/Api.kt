package com.example.inertloginmodule.api

import com.example.inertloginmodule.models.DefaultResponse
import com.example.inertloginmodule.models.LoginResponse
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @GET("insert.php/")
    fun apicall(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("mobile") mobile: String,
        @Query("emailid") emailid: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("loginToken.php/")
    fun userLogin(
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("token") token: String,
        @Field("alreadyUser") isUser:Boolean
    ): Call<LoginResponse>
}