package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.TripItemBar
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "12. Detalles del viaje" hero: full-bleed 300-tall pager, overlaid back/heart
 * circles, and a page-counter pill bottom-right -- `rgba(255,255,255,.94)`, padding 5x10,
 * 500/10 #003049, reading "1 / 6"; tapping it opens the gallery (screen 13, NEW).
 *
 * 2026-08-20 (design handoff pass): hero height 250->300, counter pill restyled from the old
 * solid-black 8dp-radius box to the spec's translucent-white pill, and it's now tappable
 * ([onOpenGallery]) instead of decorative. [onOpenGallery] defaults to a no-op so this stays
 * source-compatible if a call site doesn't wire the new Galería screen yet.
 */
@Composable
fun ShoppingItemHeader(
    images: List<String>,
    isSavedForLater: Boolean,
    onBackPressed: () -> Unit = {},
    onOpenGallery: (Int) -> Unit = {},
    onCLick: () -> Unit = {}
) {
    val colors = caribeandoColors

    if (images.isEmpty()) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp)
                    .shimmerEffect(),
            ) {

            }
        }
    } else {
        val pagerState = rememberPagerState(pageCount = {
            images.size
        })
        Box {
            Box {
                HorizontalPager(state = pagerState) { page ->
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        model = images[page],
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
                Box(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .background(colors.overlayOnPhoto, Radii.pillShape)
                        .bounceClick(scaleDown = 0.94f) { onOpenGallery(pagerState.currentPage) }
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${images.size}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        color = colors.textPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Box(Modifier.padding(16.dp)) { TripItemBar(isSavedForLater, onBackPressed, onCLick) }
        }
    }
}
