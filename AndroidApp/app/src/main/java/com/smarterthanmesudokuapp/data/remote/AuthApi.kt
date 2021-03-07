package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.remote.post.LoginPost
import com.smarterthanmesudokuapp.data.remote.post.RegisterPost
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body body: RegisterPost): Call<AuthResponse>

    @POST("/api/v1/auth/login")
    suspend fun login(@Body body: LoginPost): Call<AuthResponse>
}