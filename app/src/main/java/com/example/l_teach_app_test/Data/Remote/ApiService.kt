package com.example.l_teach_app_test.Data.Remote

import com.example.l_teach_app_test.Data.Model.LoginResponseDto
import com.example.l_teach_app_test.Data.Model.PhoneMaskDto
import com.example.l_teach_app_test.Data.Model.PostDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/v1/phone_masks")
    suspend fun getPhoneMask(): PhoneMaskDto

    @FormUrlEncoded
    @POST("api/v1/auth")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): LoginResponseDto

    @GET("api/v1/posts")
    suspend fun getPosts(): List<PostDto>

}