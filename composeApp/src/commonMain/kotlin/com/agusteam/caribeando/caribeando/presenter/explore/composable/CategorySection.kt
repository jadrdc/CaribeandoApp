package com.agusteam.caribeando.presenter.explore.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.common.loading.CategoryLoadingList
import com.agusteam.caribeando.presenter.explore.state.CategoryState
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent

@Composable
fun CategorySection(
    categoryState: CategoryState,
    onCategorySelected: (ExploreEvent.OnCategorySelected) -> Unit
) {
    if (categoryState.isLoadingCategory) {
        CategoryLoadingList()
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categoryState.categories) { item ->
                CategoryItem(
                    item = item, onSelected = { category ->
                        onCategorySelected(ExploreEvent.OnCategorySelected(category))
                    })
            }
        }
    }
}