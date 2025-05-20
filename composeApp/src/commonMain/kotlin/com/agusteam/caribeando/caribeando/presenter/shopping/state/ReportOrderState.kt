package com.agusteam.caribeando.presenter.shopping.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel

data class ReportOrderState(
    val orderId: String = "",
    val message: String = "",
    val errorModel: ErrorModel? = null,
    val isLoading: Boolean = false,
    val modalType: ModalType = ModalType.SUCCESS
) : ViewModelState {

    fun isValid(): Boolean {
        return orderId.isNotBlank() && message.isNotBlank()
    }
}

enum class ModalType {
    SUCCESS, ERROR
}