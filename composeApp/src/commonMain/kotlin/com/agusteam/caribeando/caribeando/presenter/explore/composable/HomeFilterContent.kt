package com.agusteam.caribeando.presenter.explore.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.AmountSliderPicker
import com.agusteam.caribeando.presenter.common.LinkButton
import com.agusteam.caribeando.presenter.common.SearchField
import com.agusteam.caribeando.presenter.explore.state.ExploreState
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.clear
import caribeando.composeapp.generated.resources.filter
import caribeando.composeapp.generated.resources.trip_categories
import com.agusteam.caribeando.presenter.common.ModernDateRangePicker

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeFilterContent(
    exploreState: ExploreState,
    onEventChanged: (ExploreEvent) -> Unit,
) {
    LazyColumn(
        Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Box(modifier = Modifier.padding(top = 16.dp)) {
                SearchField(
                    query = exploreState.filterState.searchText,
                    onQueryChange = {
                        onEventChanged(ExploreEvent.OnFilterSearchChanged(it))
                    },

                    )
            }
        }
        item {
            AmountSliderPicker(filterState = exploreState.filterState) {
                onEventChanged(ExploreEvent.OnSelectedFilterAmount(it))
            }
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                color = secondary,
                text = stringResource(Res.string.trip_categories),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            FlowRow(
                Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Horizontal spacing between chips
            ) {
                exploreState.categoryState.categories.forEach { category ->
                    FilterChip(selected = category.id === (exploreState.filterState.selectedCategoryModel?.id
                        ?: ""), onClick = {
                        onEventChanged(
                            ExploreEvent.OnFilterCategorySelected(category)
                        )
                    }, label = { Text(text = category.description) }, leadingIcon = {
                        AsyncImage(
                            model = category.image,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(32.dp),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(primary)
                        )
                    })
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    color = secondary,
                    text = "Cuando quisieras viajar?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                key(
                    exploreState.filterState.leavingTimeStart,
                    exploreState.filterState.returningTimeEnd
                ) {
                    ModernDateRangePicker(
                        startDate = exploreState.filterState.leavingTimeStart,
                        endDate = exploreState.filterState.returningTimeEnd,
                        onRangeSelected = { start, end ->
                            onEventChanged(
                                ExploreEvent.OnFilterSelectedDateRange(
                                    leavingDate = start,
                                    returningDate = end
                                )
                            )
                        })
                }
            }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    LinkButton(
                        text = stringResource(Res.string.clear),
                    ) {
                        onEventChanged(ExploreEvent.OnFilterCleared)
                    }

                    ActionButton(
                        text = stringResource(Res.string.filter),
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        onEventChanged(ExploreEvent.OnFilterApplied)
                    }
                }
            }
        }
    }