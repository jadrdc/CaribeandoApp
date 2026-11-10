package com.agusteam.caribeando.caribeando.presenter.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
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
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.reviews

/**
 * The handoff's "Reseñas" section (spec screen 12): 30x30 avatar, name 600/12.5, relative date
 * 400/10.5 #5F6A70, body 400/12/1.6, 16dp apart.
 *
 * 2026-08-20 (design handoff pass): `CommentModelResponse` has no date/timestamp field at all --
 * confirmed by reading the model, not assumed -- so the spec's "relative date" line is left out
 * rather than fabricated. Everything else (avatar size, name weight, body size/line-height,
 * 16dp row spacing) matches the spec exactly.
 */
@Composable
fun CommentsSection(comments: List<CommentModelResponse>, reviewCount: Int = comments.size) {
    val colors = caribeandoColors
    Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row {
            Text(
                text = stringResource(Res.string.reviews),
                color = colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = "($reviewCount reseñas)",
                color = colors.textSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }
        comments.forEach { comment ->
            CommentItem(comment)
        }
    }
}

@Composable
fun CommentItem(comment: CommentModelResponse) {
    val colors = caribeandoColors
    Row(modifier = Modifier.fillMaxWidth()) {
        if (comment.image.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(comment.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
        } else {
            val initials = comment.name
                .split(" ")
                .take(2)
                .mapNotNull { it.firstOrNull()?.toString()?.uppercase() }
                .joinToString("")

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(colors.navyFill)
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = comment.name,
                    color = colors.textPrimary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                StarRating(rating = comment.rating)
            }
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = comment.comment,
                color = colors.textBody,
                fontSize = 12.sp,
                lineHeight = 19.2.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun StarRating(rating: Int, maxRating: Int = 5) {
    val colors = caribeandoColors
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(maxRating) { index ->
            if (index < rating) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colors.star,
                    modifier = Modifier.size(14.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = colors.emptyStar,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
