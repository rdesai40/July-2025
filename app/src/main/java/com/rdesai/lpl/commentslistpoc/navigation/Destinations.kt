package com.rdesai.lpl.commentslistpoc.navigation

import kotlinx.serialization.Serializable

sealed class Destinations(val route: String) {
    object CommentsList : Destinations("commentsList")
}