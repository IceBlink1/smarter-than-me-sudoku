package com.smarterthanmesudokuapp.domain.repository.auth

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse

interface AuthRepository {
    suspend fun authenticate(login: String, password: String): Result<AuthResponse>

    suspend fun register(login: String, password: String, email: String): Result<AuthResponse>

    fun getCachedToken(): String?

    suspend fun refreshToken() : Result<AuthResponse>

    fun skipAuth()

    suspend fun resetPassword(email: String): Result<*>

    suspend fun resetPasswordCode(code: String): Result<AuthResponse>

    suspend fun setNewPassword(password: String): Result<*>
}