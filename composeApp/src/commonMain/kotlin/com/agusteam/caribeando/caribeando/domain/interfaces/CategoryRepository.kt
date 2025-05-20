package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.CategoryModel

interface CategoryRepository {
    suspend fun getCategories(): OperationResult<List<CategoryModel>>

}