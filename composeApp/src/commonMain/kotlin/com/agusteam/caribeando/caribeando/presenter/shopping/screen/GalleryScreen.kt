package com.agusteam.caribeando.presenter.shopping.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.bounceClick
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.cancel

/**
 * The handoff's "13. Galería" [NEW] -- a full-screen photo pager. Always dark (spec: "Dark in
 * both themes, ground #0B1B24"), so this deliberately does NOT read `caribeandoColors` -- it
 * would follow the system light/dark toggle, which the spec explicitly says not to do here.
 *
 * 2026-08-20 (design handoff pass): new screen, opened by tapping the "1 / 6" counter pill on
 * the trip-detail hero. Two things the spec asks for are scoped out for this pass rather than
 * faked: a per-photo caption (no caption data exists anywhere in this app -- `images` is a bare
 * `List<String>` of URLs, so the caption line is dropped rather than filled with a placeholder)
 * and the top-right favorite heart (wiring a second, independently fetched
 * `ShoppingItemDetailsViewModel` instance to stay in sync with the trip-detail screen's favorite
 * state risked a real bug I can't test here; the heart is left off rather than shipped as a dead
 * click that doesn't actually toggle anything).
 */
@Composable
fun GalleryScreen(
    images: List<String>,
    startIndex: Int,
    destiny: String,
    businessName: String,
    onBackPressed: () -> Unit
) {
    val ground = Color(0xFF0B1B24)
    val pagerState = rememberPagerState(
        initialPage = startIndex.coerceIn(0, (images.size - 1).coerceAtLeast(0)),
        pageCount = { images.size }
    )

    Box(Modifier.fillMaxSize().background(ground)) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.14f), CircleShape)
                        .bounceClick(scaleDown = 0.92f) { onBackPressed() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.cancel),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${pagerState.currentPage + 1} / ${images.size}",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(modifier = Modifier.size(36.dp))
            }

            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
                    AsyncImage(
                        model = images.getOrNull(page),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
                text = "$destiny · $businessName",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Normal
            )

            Box(Modifier.weight(1f))

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 26.dp, top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(images.size) { index ->
                    val isCurrent = index == pagerState.currentPage
                    AsyncImage(
                        model = images[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .then(
                                if (isCurrent) Modifier.border(2.dp, Color.White, RoundedCornerShape(7.dp))
                                else Modifier.alpha(0.55f)
                            )
                    )
                }
            }
        }
    }
}
