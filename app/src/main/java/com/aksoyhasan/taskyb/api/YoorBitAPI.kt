package com.aksoyhasan.taskyb.api

import com.aksoyhasan.taskyb.model.*
import com.aksoyhasan.taskyb.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoorBitAPI {
    @GET(Constants.getAllCitiesWithInfo)
    suspend fun getAllCitiesWithInfoApi(): Response<GetAllCitiesWithInfoResponse>

    @GET(Constants.getHealthCheckForTheService)
    suspend fun getHealthCheckForTheServiceApi(): Response<GetHealthCheckForTheServiceResponse>

    @GET(Constants.getRecordById)
    suspend fun getRecordByIdApi(@Query("page") pageNumber: Int): Response<GetRecordByIdResponse>
}