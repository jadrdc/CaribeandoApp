package com.agusteam.caribeando.presenter.shopping.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.shopping.composable.DateTripRowInformation
import com.agusteam.caribeando.presenter.shopping.composable.OrderItemInfoComposable
import com.agusteam.caribeando.presenter.shopping.composable.ProviderItemOrderDetail
import com.agusteam.caribeando.presenter.shopping.navigation.ShoppingOrderDetailScreenRoute
import com.agusteam.caribeando.presenter.theme.CustomFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.agency
import caribeando.composeapp.generated.resources.amount_pay
import caribeando.composeapp.generated.resources.chevron_right_small
import caribeando.composeapp.generated.resources.contact_provider
import caribeando.composeapp.generated.resources.contact_us
import caribeando.composeapp.generated.resources.payment_confirmation_code
import caribeando.composeapp.generated.resources.payment_info
import caribeando.composeapp.generated.resources.ready
import caribeando.composeapp.generated.resources.report_issue
import caribeando.composeapp.generated.resources.reservation_details
import caribeando.composeapp.generated.resources.trip_details
import com.agusteam.caribeando.caribeando.presenter.common.PendingReview
import kotlinx.serialization.json.Json

@Composable
fun ShoppingOrderDetailScreen(
    model: ShoppingOrderDetailScreenRoute,
    reportOrder: () -> Unit,
    onBackPressed: () -> Unit,
    hasItBeenEvaluted: Boolean = true,
    rateOrder: (String) -> Unit
) {


    val pagerState = rememberPagerState(pageCount = { model.galleryPhoto.size })

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column {
                Text(
                    text = stringResource(Res.string.trip_details),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CustomFontFamily(),
                        fontSize = 22.sp
                    ), modifier = Modifier.padding(bottom = 16.dp)
                )
                OrderItemInfoComposable(model.tripTitle, "")
            }
        }
        item {
            HorizontalPager(state = pagerState) { page ->
                AsyncImage(
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    model = model.galleryPhoto[page],
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }
        if (!hasItBeenEvaluted)
            item {
                PendingReview {
                    rateOrder(model.transactionId)
                }
            }
        item {
            DateTripRowInformation(model.dateFrom, model.dateTo)
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        }
        item {
            Column {
                Text(
                    text = stringResource(Res.string.reservation_details),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CustomFontFamily(),
                        fontSize = 22.sp
                    ), modifier = Modifier.padding(bottom = 16.dp)
                )
                OrderItemInfoComposable(
                    stringResource(Res.string.payment_confirmation_code),
                    model.transactionId
                )
            }
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        }
        item {
            Column {
                Text(
                    text = stringResource(Res.string.agency),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CustomFontFamily(),
                        fontSize = 22.sp
                    ), modifier = Modifier.padding(bottom = 16.dp)
                )
                ProviderItemOrderDetail(
                    businessMonth = model.businessMonth,
                    businessPhoto = model.businessPhoto,
                    businessName = model.businessName
                )
            }
        }
        item {
            Column {
                Text(
                    text = stringResource(Res.string.payment_info),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CustomFontFamily(),
                        fontSize = 22.sp
                    ),
                )

                Box(Modifier.padding(top = 16.dp)) {
                    OrderItemInfoComposable(
                        stringResource(Res.string.amount_pay),
                        formatMoney(model.amount.toInt())
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.contact_us),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CustomFontFamily(),
                        fontSize = 22.sp
                    ), modifier = Modifier
                )
                if (true == false) {

                    Row(
                        Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(Res.string.contact_provider))
                        Icon(
                            painter = painterResource(Res.drawable.chevron_right_small),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
                Row(
                    Modifier.fillMaxWidth().padding(top = 16.dp).clickable { reportOrder() },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(Res.string.report_issue))
                    Icon(
                        painter = painterResource(Res.drawable.chevron_right_small),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        item {
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
            )
        }
        item {
            Box(Modifier.padding(top = 24.dp)) {
                ActionButton(
                    text = stringResource(Res.string.ready),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onBackPressed()
                }
            }
        }
    }
}

