package com.agusteam.caribeando.caribeando.presenter.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.agusteam.caribeando.caribeando.data.model.CommentModelResponse
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary

// Data model


@Composable
fun CommentsSection(comments: List<CommentModelResponse>) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = "Comentarios",
            color = secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        comments.forEach { comment ->
            CommentItem(comment)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CommentItem(comment: CommentModelResponse) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (!comment.image.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(comment.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        } else {
            // Fallback with initials
            val initials = comment.name
                .split(" ")
                .take(2)
                .mapNotNull { it.firstOrNull()?.toString()?.uppercase() }
                .joinToString("")

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(primary)
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = comment.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                StarRating(rating = comment.rating ?: 0)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.comment.orEmpty(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun StarRating(rating: Int, maxRating: Int = 5) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(maxRating) { index ->
            if (index < rating) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
