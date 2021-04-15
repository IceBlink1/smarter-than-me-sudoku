package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.remote.post.SudokuPost
import com.smarterthanmesudokuapp.data.remote.response.SudokuResponse
import com.smarterthanmesudokuapp.data.remote.response.SudokuResponseItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface SudokuApi {

    @GET("/api/v1/content/sudokus")
    suspend fun getUserSudokus(@Header("Authorization") authtoken: String): Response<SudokuResponse>

    @GET("/api/v1/content/sudoku")
    suspend fun getUserSudoku(
        @Header("Authorization") authtoken: String,
        @Body id: Long
    ): Response<SudokuResponseItem>

    @POST("/api/v1/content/sudokus")
    suspend fun postSudokus(
        @Header("Authorization") authtoken: String,
        @Body body: List<SudokuPost>
    ): Response<ResponseBody>

    @DELETE("/api/v1/content/sudokus")
    suspend fun deleteAllSudokus(@Header("Authorization") authtoken: String)

    @DELETE("/api/v1/content/sudoku")
    suspend fun deleteSudokuById(@Header("Authorization") authtoken: String, @Body id: Long)
}