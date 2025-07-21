package com.rdesai.lpl.commentslistpoc.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Comment(
    @SerialName("postId") val postId: Int,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("body") val body: String
)
