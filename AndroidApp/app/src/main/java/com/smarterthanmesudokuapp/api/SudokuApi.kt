package com.smarterthanmesudokuapp.api

import com.smarterthanmesudokuapp.api.post.SudokuPost
import com.smarterthanmesudokuapp.api.response.SudokuResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SudokuApi {

    @GET("/api/v1/content/sudokus")
    fun getUserSudokus(@Header("Authorization") authtoken: String): Call<SudokuResponse>

    @POST("/api/v1/content/sudokus")
    fun postSudokus(@Header("Authorization") authtoken: String, @Body body: List<SudokuPost>)
}