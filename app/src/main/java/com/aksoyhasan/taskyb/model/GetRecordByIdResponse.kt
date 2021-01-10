package com.aksoyhasan.taskyb.model

import java.io.Serializable

data class GetRecordByIdResponse(
    val timeline: List<Timeline>
) : Serializable

data class Timeline(
    val countryCount: Int,
    val date: String,
    val id: String,
    val imageUrl: String,
    val mentions: List<Mention>,
    val title: String
) : Serializable

data class Mention(
    val fullname: String,
    val id: String,
    val isFollowing: Boolean,
    val profileImage: String,
    val userName: String
) : Serializable