package com.smarterthanmesudokuapp.data.remote

import android.content.SharedPreferences
import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.remote.post.CodePost
import com.smarterthanmesudokuapp.data.remote.post.LoginPost
import com.smarterthanmesudokuapp.data.remote.post.RegisterPost
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAuthDataSource @Inject constructor(
    val api: AuthApi,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    val sharedPreferences: SharedPreferences
) : AuthDataSource {

    override suspend fun authenticate(login: String, password: String): Result<AuthResponse> {
        return withContext(dispatcher) {
            val rsp = api.login(
                LoginPost(
                    login = login,
                    password = password
                )
            )
            val body = rsp.body()
            if (rsp.isSuccessful && body != null) {
                putToken(body.token)
                return@withContext Success(body)
            } else {
                return@withContext Error(
                    Exception(
                        "Error code: ${rsp.code()}.\nMessage: ${rsp.message()}"
                    )
                )
            }

        }
    }

    private fun putToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    override suspend fun register(
        login: String,
        password: String,
        email: String
    ): Result<AuthResponse> {
        return withContext(dispatcher) {
            val rsp = api.register(
                RegisterPost(
                    login = login,
                    password = password,
                    email = email
                )
            )
            val body = rsp.body()
            if (rsp.isSuccessful && body != null) {
                putToken(body.token)
                return@withContext Success(body)
            } else {
                return@withContext Error(
                    Exception(
                        "Error code: ${rsp.code()}.\nMessage: ${rsp.message()}"
                    )
                )
            }
        }
    }

    override suspend fun refreshToken(token: String): Result<AuthResponse> {
        return withContext(dispatcher) {
            val rsp = api.refreshToken(
                "Bearer_" + token
            )
            val body = rsp.body()
            if (rsp.isSuccessful && body != null) {
                putToken(body.token)
                return@withContext Success(body)
            } else {
                return@withContext Error(
                    Exception(
                        "Error code: ${rsp.code()}.\nMessage: ${rsp.message()}"
                    )
                )
            }
        }
    }

    override suspend fun resetPassword(email: String): Result<*> {
        return withContext(dispatcher) {
            val rsp = api.resetPassword(RegisterPost(email = email))
            val body = rsp.body()
            if (rsp.isSuccessful) {
                return@withContext Success(body)
            } else {
                return@withContext Error(Exception("Error code ${rsp.code()}\nMessage: ${rsp.message()}"))
            }
        }
    }

    override suspend fun resetPasswordCode(code: String): Result<AuthResponse> {
        return withContext(dispatcher) {
            val rsp = api.resetPasswordCode(body = CodePost(code))
            val body = rsp.body()
            if (rsp.isSuccessful && body != null) {
                putToken(body.token)
                return@withContext Success(body)
            } else {
                return@withContext Error(Exception("Error code ${rsp.code()}\nMessage: ${rsp.message()}"))
            }
        }
    }

    override suspend fun setNewPassword(password: String): Result<*> {
        return withContext(dispatcher) {
            val rsp =
                sharedPreferences.getString("token", null)
                    ?.let {
                        api.setNewPassword("Bearer_" + it, RegisterPost(password = password))
                    }
            val body = rsp?.body()
            if (rsp?.isSuccessful == true && body != null) {
                return@withContext Success(body)
            } else {
                return@withContext Error(Exception("Error code ${rsp?.code()}\nMessage: ${rsp?.message()}"))
            }
        }
    }
}