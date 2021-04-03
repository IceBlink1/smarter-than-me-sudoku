package com.smarterthanmesudokuapp.repository

import android.content.SharedPreferences
import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.remote.AuthDataSource
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentMap
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

    override suspend fun recoverPassword(email: String): Result<AuthResponse> {
        TODO("Not yet implemented")
    }

    override fun getCachedToken(): String? {
        return sharedPreferences.getString("token", null)
    }


}