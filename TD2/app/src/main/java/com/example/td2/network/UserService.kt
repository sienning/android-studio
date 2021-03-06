package com.example.td2.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface UserService {
    @GET("users/info")
    fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>
}