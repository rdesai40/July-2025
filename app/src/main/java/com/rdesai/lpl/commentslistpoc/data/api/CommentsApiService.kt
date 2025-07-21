package com.rdesai.lpl.commentslistpoc.data.api

import com.rdesai.lpl.commentslistpoc.data.model.Comment
import retrofit2.http.GET

interface CommentsApiService {
    @GET("posts/1/comments")
    suspend fun getComments(): List<Comment>
}