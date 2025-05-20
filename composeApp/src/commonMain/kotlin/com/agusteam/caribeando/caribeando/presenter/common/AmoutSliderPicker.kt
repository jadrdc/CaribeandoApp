package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.explore.state.ExploreFilterState
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_money

@Composable
fun AmountSliderPicker(
    filterState: ExploreFilterState,
    onFilterAmountChanged: (Float) -> Unit
) {
    Column(Modifier.padding(top = 16.dp).fillMaxWidth()) {
        BoxWithConstraints {
            val trackWidth =
                with(LocalDensity.current) { constraints.maxWidth.toFloat() } // Slider track width
            val thumbSize = 42.dp
            val thumbOffset = with(LocalDensity.current) {
                // Calculate thumb offset based on the selected value
                ((filterState.selectedAmount - filterState.minimumAmount) /
                        (filterState.maximumAmount - filterState.minimumAmount) * (trackWidth - thumbSize.toPx())).toDp()
            }

            Box {
                // Render the Slider
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent,
                        activeTrackColor = secondary,
                        thumbColor = Color.Transparent, // Hide the default thumb
                    ),
                    value = filterState.selectedAmount,
                    onValueChange = { onFilterAmountChanged(it) },
                    valueRange = filterState.minimumAmount..filterState.maximumAmount
                )

                // Render the custom thumb
                Box(
                    modifier = Modifier
                        .offset(x = thumbOffset) // Thumb offset to center based on value
                        .size(thumbSize)
                        .clip(CircleShape)
                        .border(1.dp, Color.Red, CircleShape)
                        .background(primary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(Modifier) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(Res.drawable.ic_money),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        // Display Min and Selected Values
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatMoney(filterState.minimumAmount.toInt()))
            Text(text = formatMoney(filterState.selectedAmount.toInt()))
        }
    }
}
