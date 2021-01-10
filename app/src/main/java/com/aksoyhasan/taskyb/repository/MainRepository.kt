package com.aksoyhasan.taskyb.repository

import com.aksoyhasan.taskyb.api.RetrofitInstance

class MainRepository {
    suspend fun getAllCitiesWithInfo() = RetrofitInstance.api.getAllCitiesWithInfoApi()

    suspend fun getHealthCheckForTheService() = RetrofitInstance.api.getHealthCheckForTheServiceApi()

    suspend fun getRecordById(pageNumber: Int) = RetrofitInstance.api.getRecordByIdApi(pageNumber)
}