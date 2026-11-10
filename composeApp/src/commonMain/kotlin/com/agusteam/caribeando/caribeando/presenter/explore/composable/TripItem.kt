package com.agusteam.caribeando.presenter.explore.composable

import caribeando.composeapp.generated.resources.per_person
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.reviews_count
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.FavoriteButton
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import com.agusteam.caribeando.presenter.formatDouble
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "TripCard (feed)" shared component: photo full width x 216 radius 18; heart
 * 34x34 rgba(255,255,255,.94) inset 12 from top-right; 12dp down, title 600/16 #003049 left,
 * rating right (ic_star 13 #FFB400 + value 600/12 #003049 + count 400/12 #757575); 3dp down,
 * "destino x fecha x duracion" 400/12.5 #5F6A70; 12dp down, price 600/17 #D62828 + "por persona"
 * 400/11.5 #5F6A70 left, 20x20 avatar + name 400/11.5 #5F6A70 right.
 *
 * 2026-08-19 (modernize pass): hero image corner radius moved from a flat 16dp, with a
 * soft primary-tinted shadow underneath.
 * 2026-08-19 (interactivity pass): swapped plain clickable for bounceClick so the whole card
 * gives a subtle press-scale, matching the tactile feedback added to buttons/thumbnails
 * elsewhere in this pass.
 *
 * 2026-08-20 (screens 1-17 fidelity pass): this card had only ever received a radius/shadow
 * patch for the handoff, not the actual TripCard restyle -- a prior pass's doc comment claimed
 * the "full width x 216" spec was matched, but the height here was still the old 250dp. Now
 * fixed, along with the rest of the card: the row beside the title used to show category
 * drawables (not in the spec at all, and already redundant with the chip row on ExploreScreen
 * that filters by the same categories) -- replaced with the spec's rating display, inlined
 * directly rather than reusing the old `RatingAndReviews` component, which rendered an
 * un-localized English "(N reviews)" string (the same bug `ShoppingItemOverview` was already
 * fixed for on the detail screen). The price was navy with a stray underline and no "por
 * persona" caption; now red per spec with the caption added. `TripModel` has no "duracion"
 * field (a real backend gap, tracked separately) so the info line reads "destino x fecha" only
 * -- omitted rather than faked.
 */
@Composable
fun TripItem(
    item: TripModel,
    onClick: () -> Unit,
    toggleFavorite: (ExploreEvent) -> Unit
) {
    val colors = caribeandoColors
    Column(Modifier.bounceClick(scaleDown = 0.97f) { onClick() }) {
        val cardShape = Radii.cardShape
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().height(216.dp)
                    // 2026-08-20 (dark-mode audit): the shadow tint was the raw, theme-blind navy `secondary` --
                    // not a contrast bug (a shadow doesn't need to invert like text), but the dark-mode
                    // page background is a very similar dark navy, so this tint became nearly invisible
                    // and the card lost its elevation cue entirely in dark mode. Switched to a plain
                    // black tint, which stays a conventional, visible shadow in both themes.
                    .shadow(elevation = 8.dp, shape = cardShape, ambientColor = Color.Black.copy(alpha = 0.25f), spotColor = Color.Black.copy(alpha = 0.25f))
                    .clip(cardShape),
                model = item.images.firstOrNull() ?: "",
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Box(
                Modifier.align(Alignment.TopEnd).padding(12.dp)
            ) {
                FavoriteButton(isSavedForLater = item.isSavedForLater) {
                    toggleFavorite(
                        ExploreEvent.OnShoppingItemMarked(item)
                    )
                }
            }
        }
        Column {
            Row(
                Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = colors.star,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = formatDouble(item.rating),
                        color = colors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = " " + stringResource(Res.string.reviews_count, item.reviewCount),
                        color = colors.grey500,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = "${item.destiny} · ${item.date}",
                color = colors.textSecondary,
                fontWeight = FontWeight.Normal,
                fontSize = 12.5.sp
            )
            Row(
                Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatMoney(item.price),
                        color = colors.redFill,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(Res.string.per_person),
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.5.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier.size(20.dp).clip(CircleShape),
                        model = item.businessImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = item.businessName,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.5.sp
                    )
                }
            }
        }
    }
}
