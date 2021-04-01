package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.remote.post.SudokuPost
import com.smarterthanmesudokuapp.data.remote.response.SudokuResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SudokuApi {

    @GET("/api/v1/content/sudokus")
    suspend fun getUserSudokus(@Header("Authorization") authtoken: String): Response<SudokuResponse>

    @POST("/api/v1/content/sudokus")
    suspend fun postSudokus(@Header("Authorization") authtoken: String, @Body body: List<SudokuPost>)
}