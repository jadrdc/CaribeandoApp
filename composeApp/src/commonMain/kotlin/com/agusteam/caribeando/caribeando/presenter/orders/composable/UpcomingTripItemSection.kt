package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.TripModel
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.upcoming

@Composable
fun UpcomingTripItemSection(
    upcomingTripItemList: List<TripModel>, goDetails: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        upcomingTripItemList.size
    })
    Column {
        Text(
            text = stringResource(Res.string.upcoming),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth() // Ensures proper alignment and space for the pager
        ) { page ->
            Column(
                modifier = Modifier
                    .padding(end = 16.dp) // Adds spacing between pages
                    .fillMaxWidth()
            ) {
                UpcomingTripItem(
                    item = upcomingTripItemList[page],
                    goDetails = goDetails
                )
            }
        }
    }
}
