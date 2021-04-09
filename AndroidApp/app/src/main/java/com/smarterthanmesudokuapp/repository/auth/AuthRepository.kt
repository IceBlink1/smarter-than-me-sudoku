package com.smarterthanmesudokuapp.repository.auth

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse

interface AuthRepository {
    suspend fun authenticate(login: String, password: String): Result<AuthResponse>

    suspend fun register(login: String, password: String, email: String): Result<AuthResponse>

    suspend fun recoverPassword(email: String): Result<AuthResponse>

    fun getCachedToken(): String?

    suspend fun refreshToken() : Result<AuthResponse>

    fun skipAuth()
}