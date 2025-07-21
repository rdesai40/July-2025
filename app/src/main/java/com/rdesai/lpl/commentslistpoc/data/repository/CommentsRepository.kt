package com.rdesai.lpl.commentslistpoc.data.repository

import com.rdesai.lpl.commentslistpoc.data.api.CommentsApiService
import com.rdesai.lpl.commentslistpoc.data.model.Comment
import javax.inject.Inject

class CommentsRepository @Inject constructor(val apiService: CommentsApiService){
   suspend fun getComments() : List<Comment> {
       return apiService.getComments()
   }
}