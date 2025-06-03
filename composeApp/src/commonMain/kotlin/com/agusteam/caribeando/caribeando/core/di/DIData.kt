package com.agusteam.caribeando.core.di

import com.agusteam.caribeando.data.network.services.CategoryService
import com.agusteam.caribeando.data.network.services.OrderService
import com.agusteam.caribeando.data.network.services.SignUpService
import com.agusteam.caribeando.data.network.services.PaymentService
import com.agusteam.caribeando.data.network.services.RefreshService
import com.agusteam.caribeando.data.network.services.TripProviderService
import com.agusteam.caribeando.data.network.services.TripService
import com.agusteam.caribeando.data.network.services.UserProfileService
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val networkModule: Module
expect val dataStorageDIModule: Module
val dataDiModule = module {
    single<SignUpService> { SignUpService(get()) }
    single<RefreshService> { RefreshService(get(named("refreshClient"))) }
    single<CategoryService> { CategoryService(get()) }
    single<PaymentService> { PaymentService(get()) }
    single<TripService> { TripService(get()) }
    single<TripProviderService> { TripProviderService(get()) }
    single<OrderService> { OrderService(get()) }
    single<UserProfileService> { UserProfileService(get()) }
}