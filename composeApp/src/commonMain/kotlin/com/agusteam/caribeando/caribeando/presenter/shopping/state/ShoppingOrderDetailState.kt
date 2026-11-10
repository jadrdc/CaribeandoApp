package com.agusteam.caribeando.presenter.shopping.state

import com.agusteam.caribeando.core.base.ViewModelState

data class ShoppingOrderDetailState(
    val dateFrom: String = "",
    val dateTo: String = "",
    val confirmationCode: String = "",
    val showCancelConfirmation: Boolean = false,
    val isCancelling: Boolean = false,
    val cancelResultMessage: String? = null,
    val cancelSucceeded: Boolean = false
) : ViewModelState
