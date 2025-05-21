package com.agusteam.caribeando.core.di

import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreViewModel
import com.agusteam.caribeando.presenter.home.viewmodel.HomeViewModel
import com.agusteam.caribeando.presenter.orders.viewmodels.OrderHistoryViewModel
import com.agusteam.caribeando.presenter.orders.viewmodels.WishListOrderDetailViewModel
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileViewModel
import com.agusteam.caribeando.presenter.profile.viewmodels.TripProviderViewModel
import com.agusteam.caribeando.presenter.shopping.viewmodels.RatingOrderViewModel
import com.agusteam.caribeando.presenter.shopping.viewmodels.ReportOrderViewModel
import com.agusteam.caribeando.presenter.shopping.viewmodels.ShoppingItemDetailsViewModel
import com.agusteam.caribeando.presenter.shopping.viewmodels.ShoppingOrderDetailViewModel
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginViewModel
import com.agusteam.caribeando.presenter.signup.viewmodels.SignUpViewModel
import com.agusteam.caribeando.presenter.stripe.PaymentViewModel
import com.agusteam.caribeando.presenter.wishlist.viewmodels.WishListItemViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SignUpViewModel> { SignUpViewModel(get(), get(), get(), get(),) }
    viewModel<LoginViewModel> { LoginViewModel(get(), get(), get(), get()) }
    viewModel<ExploreViewModel> { ExploreViewModel(get(), get(), get(), get(), get()) }
    viewModel<ProfileViewModel> { ProfileViewModel(get(), get(), get(), get()) }
    viewModel<OrderHistoryViewModel> { OrderHistoryViewModel(get(), get()) }
    viewModel<ShoppingItemDetailsViewModel> {
        ShoppingItemDetailsViewModel(
            get(), get(),
            get()
        )
    }
    viewModel<WishListOrderDetailViewModel> { WishListOrderDetailViewModel(get()) }
    viewModel<ShoppingOrderDetailViewModel> { ShoppingOrderDetailViewModel() }
    viewModel<WishListItemViewModel> { WishListItemViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel() }
    viewModel<RatingOrderViewModel> { RatingOrderViewModel(get()) }
    viewModel<ReportOrderViewModel> { ReportOrderViewModel(get()) }
    viewModel<PaymentViewModel> { PaymentViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel<TripProviderViewModel> { TripProviderViewModel(get(), get()) }
}