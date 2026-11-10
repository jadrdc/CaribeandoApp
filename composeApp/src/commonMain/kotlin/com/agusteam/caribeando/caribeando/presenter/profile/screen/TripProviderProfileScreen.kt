package com.agusteam.caribeando.presenter.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.BackButton
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.getTimePeriod
import com.agusteam.caribeando.presenter.getTimePeriodUnit
import com.agusteam.caribeando.presenter.profile.composable.TripProviderOverviewShimmer
import com.agusteam.caribeando.presenter.profile.viewmodels.TripProviderViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.agency_title
import caribeando.composeapp.generated.resources.current_trips
import caribeando.composeapp.generated.resources.trip_offered

/**
 * The handoff's "29. Perfil del proveedor". Was a plain white column (`ProviderOverViewItem`'s
 * shadowed card + `ProviderProfileOverview`'s Material-icon rows + `ProviderProfileCategory`'s
 * category chips, none of it from this design system) -- restyled to the spec's 150dp cover with
 * an overlapping avatar, an eyebrow/name header, a 3-cell stats strip, the description, and a
 * "Viajes actuales" TripRow list.
 *
 * 2026-08-20 (design handoff pass, part 2): the category-chip section (`ProviderProfileCategory`)
 * isn't in this spec at all -- dropped rather than kept as an unspecced leftover.
 *
 * One thing flagged rather than faked: the spec's "star 14 + rating 600/12.5 + (N reseñas)" line
 * is omitted -- `TripProviderModel` has no `rating`/`reviewCount` field (only `TripModel`, the
 * individual trip, carries those); there's no real per-provider aggregate to show.
 */
@Composable
fun TripProviderProfileScreen(
    tripProviderModel: TripProviderViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    businessId: String
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    LaunchedEffect(Unit) {
        tripProviderModel.handlerEvent(
            TripProviderViewModel.TripProviderEvent.TripProviderDetailsLoading(
                businessId
            )
        )
    }
    val state = tripProviderModel.state.collectAsStateWithLifecycle().value

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Box {
                Box(
                    Modifier.fillMaxWidth().height(150.dp)
                        .background(colors.subtleFill)
                ) {
                    state.tripProviderModel?.let {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            model = it.image,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }
                Box(Modifier.padding(16.dp)) { BackButton { onBackPressed() } }
            }
        }
        if (state.isLoading) {
            item {
                Box(Modifier.padding(16.dp)) {
                    TripProviderOverviewShimmer()
                }
            }
        } else {
            state.tripProviderModel?.let { provider ->
                item {
                    // The 70dp avatar overlaps the 150dp cover above by 30dp -- rather than
                    // stacking negative offsets across every following LazyColumn item (fragile:
                    // offset shifts render position but not layout flow, so each sibling would
                    // need its own compensating offset), this Box reserves exactly the avatar's
                    // real 40dp of flow space (70dp height - 30dp overlap) and lets the Row
                    // holding it visually overflow upward into that space. Every item after this
                    // one flows normally from the avatar's true bottom edge -- no further offsets
                    // needed anywhere else in this screen.
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Box(Modifier.height(40.dp)) {
                            Row(
                                modifier = Modifier.offset(y = (-30).dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .background(colors.cream, RoundedCornerShape(20.dp))
                                        .border(3.dp, Color.White, RoundedCornerShape(20.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.size(50.dp).clip(RoundedCornerShape(14.dp)),
                                        model = provider.avatarUrl,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                }
                                Column(
                                    modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
                                ) {
                                    Text(
                                        text = stringResource(Res.string.agency_title).uppercase(),
                                        color = colors.redFill,
                                        style = textStyles.eyebrowCaps,
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 2.dp),
                                        text = provider.name,
                                        color = colors.textPrimary,
                                        fontSize = 18.sp,
                                        lineHeight = 22.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Box(Modifier.padding(horizontal = 20.dp).padding(top = 22.dp)) {
                        ProviderStatsStrip(
                            offerTrips = provider.offerTrips,
                            activeTrips = provider.activeTrips,
                            month = provider.month,
                        )
                    }
                }
                if (provider.description.isNotEmpty())
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 20.dp),
                            text = provider.description,
                            color = colors.textBody,
                            fontSize = 13.sp,
                            lineHeight = 21.45.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                if (state.upcomingTrips.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 26.dp, bottom = 10.dp),
                            text = stringResource(Res.string.current_trips),
                            color = colors.textPrimary,
                            style = textStyles.sectionHeader,
                        )
                    }
                    items(state.upcomingTrips) { trip ->
                        Box(Modifier.padding(horizontal = 20.dp).padding(bottom = 14.dp)) {
                            ProviderTripRow(trip) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderStatsStrip(offerTrips: Long, activeTrips: Long, month: Int) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Radii.panelShape)
            .border(1.dp, colors.panelBorder, Radii.panelShape)
    ) {
        ProviderStatCell(offerTrips.toString(), stringResource(Res.string.trip_offered), Modifier.weight(1f))
        Box(Modifier.width(1.dp).height(48.dp).background(colors.rowDivider))
        ProviderStatCell(activeTrips.toString(), stringResource(Res.string.current_trips), Modifier.weight(1f))
        Box(Modifier.width(1.dp).height(48.dp).background(colors.rowDivider))
        // The spec's own caption here is the fixed "Meses en Caribeando" -- kept dynamic instead
        // (getTimePeriod already correctly switches Mes/Meses/Año/Años by magnitude) since a
        // long-tenured provider's real `month` value can be large enough to read as years, and
        // hardcoding "Meses" would then just be wrong.
        ProviderStatCell(getTimePeriodUnit(month), getTimePeriod(month), Modifier.weight(1f))
    }
}

@Composable
private fun ProviderStatCell(value: String, caption: String, modifier: Modifier = Modifier) {
    val colors = caribeandoColors
    Column(
        modifier = modifier.padding(horizontal = 15.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, color = colors.textPrimary, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = caption,
            color = colors.textSecondary,
            fontSize = 10.5.sp,
            lineHeight = 14.175.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

/**
 * The handoff's shared "TripRow (compact)" component: "Card radius 16, 1dp #EDEFF1 border,
 * 12dp padding, 12dp gap. Thumb ... 72x72 on the provider profile, radius 12." This row already
 * had the correct 72x72/radius-12 thumb, but was missing the card's own chrome entirely --
 * the same gap found on screens 23 and 26's TripRow usages (2026-08-20 screens 18-30 fidelity
 * pass).
 */
@Composable
private fun ProviderTripRow(item: TripModel, goDetails: () -> Unit) {
    val colors = caribeandoColors
    Row(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, colors.panelBorder, RoundedCornerShape(16.dp))
            .bounceClick(scaleDown = 0.98f) { goDetails() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier.size(72.dp).clip(RoundedCornerShape(12.dp)),
            model = item.images.firstOrNull() ?: "",
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column {
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = item.name,
                color = colors.textPrimary,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (item.destiny.isNotBlank())
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = item.destiny,
                    color = colors.textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
        }
    }
}
