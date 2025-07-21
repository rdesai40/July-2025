package com.rdesai.lpl.commentslistpoc.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 800, easing = LinearEasing)
        )
    )
    IconButton(onClick = onRefresh, enabled = !isLoading) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refreshing",
            modifier = if (isLoading) Modifier.rotate(rotation) else Modifier
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
