package com.agusteam.caribeando.presenter.common.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TripItemLoadingSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TripItemLoading()
        TripItemLoading()
        TripItemLoading()
        TripItemLoading()
    }
}