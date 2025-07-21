package com.rdesai.lpl.commentslistpoc.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rdesai.lpl.commentslistpoc.R
import com.rdesai.lpl.commentslistpoc.data.model.Comment
import com.rdesai.lpl.commentslistpoc.ui.theme.Dimens

@Composable
fun CommentCard(
    comment: Comment,
    profileImageUri: String,
    onProfileImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp) ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Column: Profile Image
                ProfileImage(
                    imageUri = profileImageUri,
                    onClick = { onProfileImageClick(comment.id) }
                )

                // Comment Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Column: Name & ID
                    Text(
                        text = comment.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "ID: ${comment.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                // Column: Email
                Column(
                    modifier = Modifier.width(
                        if(LocalConfiguration.current.orientation
                            == Configuration.ORIENTATION_LANDSCAPE)
                            Dimens.LandscapeWidthEmailField
                        else Dimens.PortraitWidthEmailField
                    ),
                    horizontalAlignment = Alignment.End
                ) {
                    SelectionContainer {
                        Text(
                            text = comment.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Visible
                        )
                    }
                }
            }
            //Row: Spacer & Body text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.width(60.dp))
                Text(
                    text = comment.body,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Visible
                )
            }
        }

    }
}

@Composable
private fun ProfileImage(
    imageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!imageUri.isNullOrBlank()) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                fallback = painterResource(R.drawable.person),
                placeholder = painterResource(R.drawable.person),
                error = painterResource(R.drawable.person)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.person),
                contentDescription = "Default Profile",
                modifier = Modifier.size(45.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CommentCardPreview() {
    MaterialTheme {
        CommentCard(
            comment = Comment(
                postId = 1,
                id = 1,
                name = "id labore ex et quam laborum",
                email = "Eliseo@gardner.biz",
                body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
            ),
            profileImageUri = "",
            onProfileImageClick = {}
        )
    }
}
