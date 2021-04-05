package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse

interface AuthDataSource {

    suspend fun authenticate(login: String, password: String): Result<AuthResponse>

    suspend fun register(login: String, password: String, email: String) : Result<AuthResponse>

    suspend fun recoverPassword(email: String) : Result<AuthResponse>

    suspend fun refreshToken(token: String) : Result<AuthResponse>
}