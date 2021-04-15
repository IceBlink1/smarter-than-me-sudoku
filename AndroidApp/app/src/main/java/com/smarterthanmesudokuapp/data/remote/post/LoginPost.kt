package com.smarterthanmesudokuapp.data.remote.post

import com.google.gson.annotations.SerializedName

data class LoginPost(
    @SerializedName("username") val login: String,
    @SerializedName("password") val password: String
)