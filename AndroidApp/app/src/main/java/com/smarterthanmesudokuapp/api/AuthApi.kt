package com.smarterthanmesudokuapp.api

import com.smarterthanmesudokuapp.api.post.LoginPost
import com.smarterthanmesudokuapp.api.post.RegisterPost
import com.smarterthanmesudokuapp.api.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/register")
    fun register(@Body body: RegisterPost): Call<AuthResponse>

    @POST("/api/v1/auth/login")
    fun login(@Body body: LoginPost): Call<AuthResponse>
}