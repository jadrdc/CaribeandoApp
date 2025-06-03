package com.agusteam.caribeando.presenter.common.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CategoryLoadingList() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CategoryIconLoading()
        CategoryIconLoading()
        CategoryIconLoading()
        CategoryIconLoading()
        CategoryIconLoading()
    }
}