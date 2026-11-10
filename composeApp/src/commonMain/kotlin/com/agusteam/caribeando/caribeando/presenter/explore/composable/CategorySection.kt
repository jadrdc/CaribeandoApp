package com.agusteam.caribeando.presenter.explore.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.presenter.common.Chip
import com.agusteam.caribeando.presenter.common.loading.CategoryLoadingList
import com.agusteam.caribeando.presenter.explore.state.CategoryState
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.category_all
import caribeando.composeapp.generated.resources.ic_search

/**
 * 2026-08-20 (design handoff pass): restyled to the handoff's pill "Chip" (radius 999, selected
 * = navy fill/white content, unselected = 1dp border/navy content) instead of the old vertical
 * icon-over-label layout.
 *
 * The handoff spec's sample text calls for a synthetic "Todo" chip using `ic_search` ahead of
 * the real categories. This is added here as a purely visual, composable-local synthetic item --
 * it is NOT added to `categoryState.categories` and does not touch ExploreViewModel at all.
 * Tapping it dispatches `OnCategorySelected(CategoryModel(id = "", description = "Todo"))`, which
 * flows through the existing `updateSelectedCategory` unchanged: id = "" is already the
 * "no category filter" sentinel used by `initialLoad()`'s `categoryId` param, so this reuses
 * existing "show everything" behavior rather than inventing new business logic. Its selected
 * state is derived by comparing `categoryState.selectedCategoryModel?.id` to "", since no real
 * category can have a blank id.
 */
@Composable
fun CategorySection(
    categoryState: CategoryState,
    onCategorySelected: (ExploreEvent.OnCategorySelected) -> Unit
) {
    if (categoryState.isLoadingCategory) {
        CategoryLoadingList()
    } else {
        val allLabel = stringResource(Res.string.category_all)
        val isAllSelected = categoryState.selectedCategoryModel == null ||
            categoryState.selectedCategoryModel.id.isBlank()

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Chip(
                    title = allLabel,
                    isSelected = isAllSelected,
                    icon = Res.drawable.ic_search,
                    onClick = {
                        onCategorySelected(
                            ExploreEvent.OnCategorySelected(
                                CategoryModel(id = "", description = allLabel, isSelected = true)
                            )
                        )
                    }
                )
            }
            items(categoryState.categories) { item ->
                Chip(
                    title = item.description,
                    isSelected = item.isSelected,
                    iconUrl = item.image,
                    onClick = {
                        onCategorySelected(ExploreEvent.OnCategorySelected(item))
                    }
                )
            }
        }
    }
}
