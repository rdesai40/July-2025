package com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdesai.lpl.commentslistpoc.data.repository.CommentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CommentsListViewModel @Inject constructor(val repository: CommentsRepository): ViewModel() {

    private val _viewState = MutableStateFlow<CommentsViewState>(CommentsViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _events = Channel<CommentsEvent>()
    val events = _events.receiveAsFlow()


    init {
        handleAction(CommentsAction.LoadComments)
    }

    fun handleAction(action: CommentsAction) {
        when (action) {
            is CommentsAction.LoadComments -> loadComments()
            is CommentsAction.RefreshComments -> refreshComments()
            is CommentsAction.RequestImagePicker -> requestImagePicker(action.commentId)
            is CommentsAction.UpdateProfileImage -> updateProfileImage(action.commentId, action.imageUrl)
        }
    }

    private fun loadComments() {
        viewModelScope.launch {
            try {
                val comments = repository.getComments()
                if (comments.isEmpty()) {
                    _viewState.value = CommentsViewState.Empty()
                } else {
                    _viewState.value = CommentsViewState.Success(
                        comments = comments,
                        profileImages = getCurrentProfileImages()
                    )
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Something went wrong, please try again later"
                _viewState.value = CommentsViewState.Error(errorMessage)
                _events.send(CommentsEvent.ShowMessage(errorMessage))
            }
        }
    }

    private fun refreshComments() {
        _events.trySend(CommentsEvent.ShowRefreshing)
        viewModelScope.launch {
            try {
                val comments = repository.getComments()
                if (comments.isEmpty()) {
                    _viewState.value = CommentsViewState.Empty()
                } else {
                    _viewState.value = CommentsViewState.Success(
                        comments = comments,
                        profileImages = getCurrentProfileImages()
                    )
                }
                _events.send(CommentsEvent.ShowMessage("Refresh successful| Your comments has been updated"))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Refresh failed, please try again later"
                _events.send(CommentsEvent.ShowMessage(errorMessage))
            } finally {
                _events.send(CommentsEvent.HideRefreshing)
            }
        }
    }

    private fun requestImagePicker(commentId: Int) {
        viewModelScope.launch {
            _events.send(CommentsEvent.OpenImagePicker(commentId))
        }
    }

    private fun updateProfileImage(commentId: Int, imageUrl: String) {
        viewModelScope.launch {
            val currentState = _viewState.value
            if(currentState is CommentsViewState.Success){
                val updatedImages = currentState.profileImages.toMutableMap()
                updatedImages[commentId] = imageUrl
                _viewState.value = currentState.copy(profileImages = updatedImages)
            }
        }
    }
    private fun getCurrentProfileImages(): Map<Int, String> {
        val currentState = _viewState.value
        return if(currentState is CommentsViewState.Success) {
            currentState.profileImages
        } else {
            emptyMap()
        }
    }

}