package com.rdesai.lpl.commentslistpoc.presentation.comments.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel.CommentsAction
import com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel.CommentsEvent
import com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel.CommentsListViewModel
import com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel.CommentsViewState
import com.rdesai.lpl.commentslistpoc.presentation.components.CommentCard
import com.rdesai.lpl.commentslistpoc.presentation.components.TopBar
import com.rdesai.lpl.commentslistpoc.presentation.components.ErrorScreen
import com.rdesai.lpl.commentslistpoc.presentation.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsListScreen(
    modifier: Modifier = Modifier,
    viewModel: CommentsListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentCommentId by remember { mutableStateOf<Int?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            currentCommentId?.let { commentId ->
                viewModel.handleAction(CommentsAction.UpdateProfileImage(commentId, uri.toString()))
            }
        }
        currentCommentId = null
    }

    //Handle Events
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event) {
                is CommentsEvent.OpenImagePicker -> {
                    currentCommentId = event.commentId
                    imagePickerLauncher.launch("image/*")
                }
                is CommentsEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                CommentsEvent.HideRefreshing -> isRefreshing = false
                CommentsEvent.ShowRefreshing -> isRefreshing = true
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.handleAction(CommentsAction.RefreshComments) }
            ) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        CommentsContent(
            viewState = viewState,
            innerPadding = innerPadding,
            onAction = viewModel::handleAction
        )
    }
}

@Composable
fun CommentsContent(
    viewState: CommentsViewState,
    innerPadding: PaddingValues,
    onAction: (CommentsAction) -> Unit
) {
    when (viewState) {
        is CommentsViewState.Empty -> ErrorScreen(Modifier.padding(innerPadding), viewState.message, false)
        is CommentsViewState.Error -> ErrorScreen(Modifier.padding(innerPadding), viewState.message, true)
        CommentsViewState.Loading -> LoadingScreen()
        is CommentsViewState.Success -> CommentsList(Modifier.padding(innerPadding), viewState, onAction)
    }
}

@Composable
fun CommentsList(
    modifier: Modifier = Modifier,
    viewState: CommentsViewState.Success,
    onAction: (CommentsAction) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp,),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = viewState.comments,
            key = { it.id }
        ) { comment ->
            CommentCard(
                comment,
                viewState.profileImages[comment.id] ?: "",
                onProfileImageClick = { onAction(CommentsAction.RequestImagePicker(it)) }
            )
        }
    }
}