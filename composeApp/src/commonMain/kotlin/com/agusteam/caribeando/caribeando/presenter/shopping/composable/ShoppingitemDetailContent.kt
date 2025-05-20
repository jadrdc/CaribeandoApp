package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel

@Composable
fun ShoppingitemContent(itemsDetails: List<ShoppingDetailModel>, modifier: Modifier) {
    Column(modifier) {
        itemsDetails.forEach { shoppingItem ->
            Box(Modifier.padding(top = 16.dp)) {
                ShoppingItemDetail(shoppingItem)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
    }

}