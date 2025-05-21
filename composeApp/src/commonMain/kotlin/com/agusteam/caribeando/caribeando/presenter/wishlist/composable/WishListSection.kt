package com.agusteam.caribeando.presenter.wishlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.TripModel
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.favorite
import com.agusteam.caribeando.caribeando.presenter.common.EmptyState

@Composable
fun WishListSection(favoriteItems: List<TripModel>, goDetails: (TripModel) -> Unit) {
    Column(Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
        Text(
            text = stringResource(Res.string.favorite),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (favoriteItems.isNotEmpty()){
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 16.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(favoriteItems) { item ->
                    WishItem(item) {
                        goDetails(item)
                    }
                }
            }
    } else{
        EmptyState(
            message = "No hay viajes que mostrar",
            actionText = "Intente nuevamente"
        )
    }
    }
}