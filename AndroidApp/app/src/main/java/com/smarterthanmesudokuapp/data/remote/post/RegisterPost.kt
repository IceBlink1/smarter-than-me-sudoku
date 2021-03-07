package com.smarterthanmesudokuapp.data.remote.post

import com.google.gson.annotations.SerializedName

data class RegisterPost(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)