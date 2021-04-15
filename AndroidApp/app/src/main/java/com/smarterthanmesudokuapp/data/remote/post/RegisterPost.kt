package com.smarterthanmesudokuapp.data.remote.post

import com.google.gson.annotations.SerializedName

data class RegisterPost(
    @SerializedName("username") val login: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("email") val email: String? = null
)