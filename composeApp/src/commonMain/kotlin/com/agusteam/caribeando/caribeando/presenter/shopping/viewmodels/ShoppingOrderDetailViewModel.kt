package com.agusteam.caribeando.presenter.shopping.viewmodels

import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.presenter.shopping.state.ShoppingOrderDetailState

class ShoppingOrderDetailViewModel:
    GenericViewModel<ShoppingOrderDetailState, ShoppingOrderDetailEvent>(ShoppingOrderDetailState())

sealed interface ShoppingOrderDetailEvent