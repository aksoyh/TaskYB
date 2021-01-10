package com.aksoyhasan.taskyb.model

import java.io.Serializable

data class GetAllCitiesWithInfoResponse(
    val featured: List<Featured>
) : Serializable

data class Featured(
    val city: String,
    val country: String,
    val id: String,
    val imageUrl: String
) : Serializable