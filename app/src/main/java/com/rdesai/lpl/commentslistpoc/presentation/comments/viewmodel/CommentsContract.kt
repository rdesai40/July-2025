package com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel

import com.rdesai.lpl.commentslistpoc.data.model.Comment

sealed class CommentsViewState {
    object Loading : CommentsViewState()
    data class Error(val message: String) : CommentsViewState()
    data class Empty(val message: String = "No comments found") : CommentsViewState()

    data class Success(
        val comments: List<Comment>,
        val profileImages: Map<Int, String> = emptyMap()
    ) : CommentsViewState()
}

sealed class CommentsEvent {
    object ShowRefreshing : CommentsEvent()
    object HideRefreshing : CommentsEvent()
    data class ShowError(val message: String) : CommentsEvent()
    data class OpenImagePicker(val commentId: Int) : CommentsEvent()
}

sealed class CommentsAction {
    object LoadComments : CommentsAction()
    object RefreshComments : CommentsAction()
    data class RequestImagePicker(val commentId: Int) : CommentsAction()
    data class UpdateProfileImage(val commentId: Int, val imageUrl: String) : CommentsAction()
}
