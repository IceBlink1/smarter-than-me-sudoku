package com.smarterthanmesudokuapp.domain.repository.auth

import android.content.SharedPreferences
import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.remote.AuthDataSource
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val dataSource: AuthDataSource,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {

    override suspend fun authenticate(login: String, password: String): Result<AuthResponse> {
        return withContext(dispatcher) {
            dataSource.authenticate(login, password)
        }
    }

    override suspend fun register(
        login: String,
        password: String,
        email: String
    ): Result<AuthResponse> {
        return withContext(dispatcher) {
            dataSource.register(login, password, email)
        }
    }

    override fun getCachedToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    override suspend fun refreshToken(): Result<AuthResponse> {
        return withContext(dispatcher) {
            val token = getCachedToken()
                ?: return@withContext Error(Exception("Error: cached token is null"))
            return@withContext dataSource.refreshToken(token)
        }
    }

    override fun skipAuth() {
        sharedPreferences.edit().putString("token", null).apply()
    }

    override suspend fun resetPassword(email: String): Result<*> {
        return withContext(dispatcher) {
            dataSource.resetPassword(email)
        }
    }

    override suspend fun resetPasswordCode(code: String): Result<AuthResponse> {
        return withContext(dispatcher) {
            dataSource.resetPasswordCode(code)
        }
    }

    override suspend fun setNewPassword(password: String): Result<*> {
        return withContext(dispatcher) {
            dataSource.setNewPassword(password)
        }
    }

}