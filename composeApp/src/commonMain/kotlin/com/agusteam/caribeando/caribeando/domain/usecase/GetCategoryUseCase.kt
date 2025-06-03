package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.CategoryRepository
import com.agusteam.caribeando.domain.models.CategoryModel

class GetCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(): OperationResult<List<CategoryModel>> {
        return repository.getCategories()
    }
}