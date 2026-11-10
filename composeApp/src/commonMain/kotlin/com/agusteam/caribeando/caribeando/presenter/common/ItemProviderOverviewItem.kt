package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.getTimePeriod
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.agency
import caribeando.composeapp.generated.resources.chevron_right_small

/**
 * The handoff's provider row (spec screens 12 and 19): 1dp #EDEFF1 panel radius 16, 13dp
 * padding, 42x42 avatar circle, name 600/13.5, a subtitle line 400/11.5 #5F6A70, chevron 16.
 *
 * 2026-08-20 (design handoff pass): was a bare row with a HorizontalDivider underneath, no panel
 * or chevron. Restyled to the spec's bordered panel with a trailing chevron; kept the existing
 * subtitle content (month-derived "Nace hace N meses"-style text via `getTimePeriod`) rather than
 * the spec's "Agencia - 4.9 (127)" rating line, since `ItemProviderState` here carries no
 * provider rating/review-count field to show real data with -- that's a real gap, not something
 * to fake. Signature unchanged (2 existing call sites: ShoppingItemDetailScreen, task-26's
 * OrderItemDetailsScreen).
 */
@Composable
fun ItemProviderOverviewItem(
    modifier: Modifier = Modifier,
    state: ItemProviderState,
    goProviderProfile: () -> Unit
) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(1.dp, colors.panelBorder, Radii.panelShape)
            .bounceClick(scaleDown = 0.98f) { goProviderProfile() }
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        AsyncImage(
            modifier = Modifier.size(42.dp).clip(CircleShape),
            model = state.businessImage,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        Row(
            modifier = modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.agency) + " " + state.businessName,
                    color = colors.textPrimary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = "${state.month} ${getTimePeriod(state.month)}",
                    color = colors.textSecondary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Icon(
                painter = painterResource(Res.drawable.chevron_right_small),
                contentDescription = null,
                tint = colors.iconInactive,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Serializable
data class ItemProviderState(
    val businessImage: String,
    val businessName: String,
    val month: Int,
)
