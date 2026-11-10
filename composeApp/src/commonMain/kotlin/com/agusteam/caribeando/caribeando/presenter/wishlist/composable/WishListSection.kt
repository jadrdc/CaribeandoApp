package com.agusteam.caribeando.presenter.wishlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.explore_trips_action
import caribeando.composeapp.generated.resources.favorite
import caribeando.composeapp.generated.resources.favorites_count_one
import caribeando.composeapp.generated.resources.favorites_count_other
import caribeando.composeapp.generated.resources.favorites_empty_subtitle
import caribeando.composeapp.generated.resources.favorites_empty_title
import com.agusteam.caribeando.caribeando.presenter.common.EmptyState

/**
 * The handoff's "26. Favoritos" / "27. Favoritos — vacío". Was a 2-column grid of 150dp cards
 * with a hardcoded "No hay viajes que mostrar" empty state copied from the (unrelated) Viajes
 * empty state -- restyled to the spec's title + "N excursiones guardadas" count, a vertical
 * TripRow list, and Favoritos' own real empty-state copy.
 *
 * 2026-08-20 (design handoff pass, part 2): [onRemove] is new -- wires the trailing heart circle
 * on each row to the real unmark-favorite flow (`WishListItemViewModel.WishListEvent.RemoveFavorite`).
 */
@Composable
fun WishListSection(
    favoriteItems: List<TripModel>,
    onRemove: (String) -> Unit,
    onExploreClick: () -> Unit = {},
    goDetails: (TripModel) -> Unit
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    Column(Modifier.padding(horizontal = 20.dp, vertical = 32.dp)) {
        // 2026-08-20 (screens 18-30 fidelity pass): was a one-off 18sp/600 -- every other
        // tab-root title (Explorar, Viajes, Perfil) uses the shared 26sp/600
        // `screenTitleTabRoot`, so this one stood out as visibly smaller than its siblings.
        Text(
            text = stringResource(Res.string.favorite),
            color = colors.textPrimary,
            style = textStyles.screenTitleTabRoot,
        )
        if (favoriteItems.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = if (favoriteItems.size == 1)
                    stringResource(Res.string.favorites_count_one, favoriteItems.size)
                else
                    stringResource(Res.string.favorites_count_other, favoriteItems.size),
                color = colors.textSecondary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Normal
            )
            LazyColumn(
                modifier = Modifier.padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                items(favoriteItems, key = { it.id }) { item ->
                    WishItem(item, onRemove = { onRemove(item.id) }) {
                        goDetails(item)
                    }
                }
            }
        } else {
            EmptyState(
                message = stringResource(Res.string.favorites_empty_title),
                subtitle = stringResource(Res.string.favorites_empty_subtitle),
                actionText = stringResource(Res.string.explore_trips_action),
                onAction = onExploreClick
            )
        }
    }
}
