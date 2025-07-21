package com.rdesai.lpl.commentslistpoc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rdesai.lpl.commentslistpoc.presentation.comments.screens.CommentsListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.CommentsList.route
    ) {
        composable(Destinations.CommentsList.route) {
            CommentsListScreen()
        }
    }
}