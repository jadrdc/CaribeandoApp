package com.agusteam.caribeando.presenter.explore.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.CategoryModel

data class CategoryState(
    val selectedCategoryModel: CategoryModel? = null,
    val isLoadingSkeleton: Boolean = true,
    val isLoadingCategory:Boolean=true,
    val categories: List<CategoryModel> = listOf(),
) : ViewModelState
