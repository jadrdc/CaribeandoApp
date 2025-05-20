package com.agusteam.caribeando.presenter.shopping.state

import com.agusteam.caribeando.core.base.ViewModelState

data class ShoppingOrderDetailState(
    val dateFrom: String = "",
    val dateTo: String = "",
    val confirmationCode: String = ""
) : ViewModelState
