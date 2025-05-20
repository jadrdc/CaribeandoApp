package com.agusteam.caribeando.presenter.common


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.agusteam.caribeando.domain.models.TripProviderModel
import com.agusteam.caribeando.presenter.getTimePeriod
import com.agusteam.caribeando.presenter.getTimePeriodUnit
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.current_trips
import caribeando.composeapp.generated.resources.trip_offered

@Composable
fun ProviderStatsItem(tripProviderModel: TripProviderModel) {
    Column(
        modifier = Modifier
    ) {
        ProviderStatItem(
            value = tripProviderModel.offerTrips.toInt().toString(),
            label = stringResource(Res.string.trip_offered)
        )
        ProviderStatItem(
            value = tripProviderModel.activeTrips.toInt().toString(),
            label = stringResource(Res.string.current_trips)
        )
        ProviderStatItem(
            showDivider = false,
            value = getTimePeriodUnit(tripProviderModel.month),
            label = getTimePeriod(tripProviderModel.month)
        )
    }
}