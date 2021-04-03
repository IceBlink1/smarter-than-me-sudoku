package com.smarterthanmesudokuapp.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.remote.response.AuthResponse
import com.smarterthanmesudokuapp.repository.AuthRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel() {
    private val loginMutableLiveData: MutableLiveData<AuthResponse> = MutableLiveData()

    val loginLiveData: LiveData<AuthResponse>
        get() = loginMutableLiveData

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

}