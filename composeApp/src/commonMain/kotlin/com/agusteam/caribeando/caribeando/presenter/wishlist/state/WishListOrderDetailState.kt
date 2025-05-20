package com.agusteam.caribeando.presenter.wishlist.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.presenter.common.ItemProviderState

data class WishListOrderDetailState(
    val itemProviderState: ItemProviderState = ItemProviderState("", "", 0),
    val isLoadingContent: Boolean = false, val includedServices: List<String> = listOf(),
) : ViewModelState
