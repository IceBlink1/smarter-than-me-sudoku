package com.smarterthanmesudokuapp.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import com.smarterthanmesudokuapp.repository.auth.AuthRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
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
            } else if (auth is Error) {
                Timber.e(auth.exception)
            }
        }
    }

    fun loginCached(): String? {
        return authRepository.getCachedToken()
    }

    fun refreshToken() {
        if (loginCached() != null) {
            viewModelScope.launch {
                val auth = authRepository.refreshToken()
                when (auth) {
                    is Success -> loginStateMutableLiveData.postValue(AuthState.AUTHENTICATED)
                    is Error -> loginStateMutableLiveData.postValue(AuthState.NOT_AUTHENTICATED)
                }
            }
        } else {
            loginStateMutableLiveData.postValue(AuthState.NOT_AUTHENTICATED)
        }
    }


    enum class AuthState {
        AUTHENTICATED,
        NOT_AUTHENTICATED,
        PENDING,
        SKIPPED
    }

}