package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.toDomainModel
import com.agusteam.caribeando.data.network.services.CategoryService
import com.agusteam.caribeando.domain.interfaces.CategoryRepository
import com.agusteam.caribeando.domain.models.CategoryModel

class CategoryRepositoryImpl(private val service: CategoryService):CategoryRepository {
    override suspend fun getCategories(): OperationResult<List<CategoryModel>> {
        return try {
            when (val result = service.getCategories()) {
                is OperationResult.Success -> {
                    val model = result.data.map {
                        it.toDomainModel()
                    }
                    OperationResult.Success(model)
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}