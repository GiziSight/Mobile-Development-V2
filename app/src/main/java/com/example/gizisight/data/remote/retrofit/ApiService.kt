package com.example.gizisight.data.remote.retrofit

import com.example.gizisight.data.remote.response.ArticleResponse
import com.example.gizisight.data.remote.response.GetUserResponse
import com.example.gizisight.data.remote.response.LoginResponse
import com.example.gizisight.data.remote.response.MessageResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //    endpoint Register
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(
        @Field("email") name: String,
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): Call<MessageResponse>

    //    endpoint login
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("getUser")
    fun getUser(
        @Query("email") email: String,
    ): Call<GetUserResponse>

    @GET("api/search")
    fun getArticle(
        @Query("query") query: String,
    ): Call<ArticleResponse>

//    @Multipart
//    @POST("stories")
//    fun postStory(
//        @Header("Authorization") token: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody
//    ): Call<RegisterResponse>

}