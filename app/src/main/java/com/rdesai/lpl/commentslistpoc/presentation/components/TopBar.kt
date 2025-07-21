package com.rdesai.lpl.commentslistpoc.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier, isRefreshing: Boolean, onRefresh: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Comments",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            RefreshIconSpinning(isRefreshing, onRefresh)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier.fillMaxWidth()
    )

}

@Composable
fun RefreshIconSpinning(isLoading: Boolean, onRefresh: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (isLoading) 360f else 0f,
        animationSpec = if (isLoading)
            infiniteRepeatable(
                animation = tween(800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        else
            tween(0), // no animation when not loading
        label = "Refresh Rotation"
    )

    IconButton(onClick = onRefresh, enabled = !isLoading) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refreshing",
            modifier = Modifier.rotate(rotation)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommentsTopBarPreview() {
    MaterialTheme {
        TopBar(isRefreshing = false, onRefresh = {})
    }
}
