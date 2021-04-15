package com.smarterthanmesudokuapp.ui.fragments.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import com.smarterthanmesudokuapp.domain.repository.auth.AuthRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel() {
    private val loginMutableLiveData: MutableLiveData<AuthResponse> = MutableLiveData()

    val loginLiveData: LiveData<AuthResponse>
        get() = loginMutableLiveData

    private val loginStateMutableLiveData: MutableLiveData<AuthState> = MutableLiveData()

    val loginStateLiveData: LiveData<AuthState>
        get() = loginStateMutableLiveData

    fun login(login: String, password: String) {
        viewModelScope.launch {
            val auth = authRepository.authenticate(login, password)
            if (auth is Success) {
                loginMutableLiveData.postValue(auth.data)
                loginStateMutableLiveData.postValue(AuthState.AUTHENTICATED)
            } else if (auth is Error) {
                Timber.e(auth.exception)
            }
        }
    }

    fun loginCached(): String? {
        return authRepository.getCachedToken()
    }

    fun skipAuth() {
        authRepository.skipAuth()
        loginStateMutableLiveData.postValue(AuthState.SKIPPED)
    }

    fun logout() {
        authRepository.skipAuth()
        loginStateMutableLiveData.postValue(AuthState.NOT_AUTHENTICATED)
    }

    fun refreshToken() {
        if (loginCached() != null) {
            viewModelScope.launch {
                when (val auth = authRepository.refreshToken()) {
                    is Success -> {
                        loginStateMutableLiveData.postValue(AuthState.AUTHENTICATED)
                        loginMutableLiveData.postValue(auth.data)
                    }
                    is Error -> {
                        if (loginStateLiveData.value != AuthState.SKIPPED) {
                            loginStateMutableLiveData.postValue(
                                AuthState.NOT_AUTHENTICATED
                            )
                        }
                    }
                }
            }
        } else if (loginStateLiveData.value != AuthState.SKIPPED) {
            loginStateMutableLiveData.postValue(AuthState.NOT_AUTHENTICATED)
        }
    }

    fun register(
        login: String,
        password: String,
        email: String
    ) {
        viewModelScope.launch {
            when (val auth = authRepository.register(login, password, email)) {
                is Success -> {
                    loginStateMutableLiveData.postValue(AuthState.AUTHENTICATED)
                    loginMutableLiveData.postValue(auth.data)
                }
                is Error -> loginStateMutableLiveData.postValue(AuthState.NOT_AUTHENTICATED)
            }
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            val a = authRepository.resetPassword(email)
            when (a) {
                is Success -> {
                    loginStateMutableLiveData.postValue(AuthState.PENDING)
                }
                is Error -> {
                    Timber.e(a.exception)
                    loginStateMutableLiveData.postValue(AuthState.NOT_AUTHENTICATED)
                }
            }
        }
    }

    fun recoverPasswordCode(code: String) {
        viewModelScope.launch {
            when (val auth = authRepository.resetPasswordCode(code)) {
                is Success -> {
                    loginStateMutableLiveData.postValue(AuthState.SETTING_PASSWORD)
                    loginMutableLiveData.postValue(auth.data)
                }
                is Error -> {
                    Timber.e(auth.exception)
                }
            }
        }
    }

    fun setNewPassword(password: String) {
        viewModelScope.launch {
            when (val a = authRepository.setNewPassword(password)) {
                is Success -> {
                    loginStateMutableLiveData.postValue(AuthState.AUTHENTICATED)
                }
                is Error -> Timber.e(a.exception)
            }
        }
    }

    enum class AuthState {
        AUTHENTICATED,
        NOT_AUTHENTICATED,
        PENDING,
        SETTING_PASSWORD,
        SKIPPED
    }

}