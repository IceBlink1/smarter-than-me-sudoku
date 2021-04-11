package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.remote.post.CodePost
import com.smarterthanmesudokuapp.data.remote.post.LoginPost
import com.smarterthanmesudokuapp.data.remote.post.RegisterPost
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/unlogged/register")
    suspend fun register(@Body body: RegisterPost): Response<AuthResponse>

    @POST("/api/v1/auth/unlogged/login")
    suspend fun login(@Body body: LoginPost): Response<AuthResponse>

    @POST("/api/v1/auth/refresh_token")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<AuthResponse>

    @POST("/api/v1/auth/unlogged/reset_password")
    suspend fun resetPassword(@Body body: RegisterPost): Response<ResponseBody>

    @POST("/api/v1/auth/unlogged/reset_password_code")
    suspend fun resetPasswordCode(@Body body: CodePost): Response<AuthResponse>

    @POST("/api/v1/auth/reset_password")
    suspend fun setNewPassword(
        @Header("Authorization") token: String,
        @Body password: RegisterPost
    ): Response<ResponseBody>
}