package com.agusteam.caribeando.core.di

import com.agusteam.caribeando.data.imp.CategoryRepositoryImpl
import com.agusteam.caribeando.data.imp.LocalStorageDataStore
import com.agusteam.caribeando.data.imp.LoginRepositoryImpl
import com.agusteam.caribeando.data.imp.OrderRepositoryImp
import com.agusteam.caribeando.data.imp.PaymentRepositoryImp
import com.agusteam.caribeando.data.imp.TokenRepositoryImp
import com.agusteam.caribeando.data.imp.TripProviderRepositoryImp
import com.agusteam.caribeando.data.imp.TripRepositoryImp
import com.agusteam.caribeando.data.imp.UserProfileRepositoryImp
import com.agusteam.caribeando.domain.interfaces.CategoryRepository
import com.agusteam.caribeando.domain.interfaces.LocalStoragePreferenceRepository
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.interfaces.OrderRepository
import com.agusteam.caribeando.domain.interfaces.PaymentRepository
import com.agusteam.caribeando.domain.interfaces.TokenRepository
import com.agusteam.caribeando.domain.interfaces.TripProviderRepository
import com.agusteam.caribeando.domain.interfaces.TripRepository
import com.agusteam.caribeando.domain.interfaces.UserProfileRepository
import com.agusteam.caribeando.domain.usecase.CancelPaymentOrderUseCase
import com.agusteam.caribeando.domain.usecase.CreatePendingPaymentOrderUseCase
import com.agusteam.caribeando.domain.usecase.FillUserInfoUseCase
import com.agusteam.caribeando.domain.usecase.GetCategoryUseCase
import com.agusteam.caribeando.domain.usecase.GetPaginatedTripsUseCase
import com.agusteam.caribeando.domain.usecase.GetPastTripOrderUseCase
import com.agusteam.caribeando.domain.usecase.GetLocalProfileUseCase
import com.agusteam.caribeando.domain.usecase.GetStripePaymentIntentUseCase
import com.agusteam.caribeando.domain.usecase.GetTripFavoriteListUseCase
import com.agusteam.caribeando.domain.usecase.GetTripProviderDetailsUseCase
import com.agusteam.caribeando.domain.usecase.GetTripsIncludedServicesUseCase
import com.agusteam.caribeando.domain.usecase.GetUpcomingTripByProviderUseCase
import com.agusteam.caribeando.domain.usecase.GetUpcomingTripOrderUseCase
import com.agusteam.caribeando.domain.usecase.GetUserProfileUseCase
import com.agusteam.caribeando.domain.usecase.GoogleSignInUseCase
import com.agusteam.caribeando.domain.usecase.LoginUseCase
import com.agusteam.caribeando.domain.usecase.LogoutUseCase
import com.agusteam.caribeando.domain.usecase.MarkFavoriteTripUseCase
import com.agusteam.caribeando.domain.usecase.ProcessSuccessPaymentOrderUseCase
import com.agusteam.caribeando.domain.usecase.RatingOrderUseCase
import com.agusteam.caribeando.domain.usecase.ReportOrderUseCase
import com.agusteam.caribeando.domain.usecase.RequestResetPasswordEmailUseCase
import com.agusteam.caribeando.domain.usecase.SaveLocalDataUseCase
import com.agusteam.caribeando.domain.usecase.SaveTokenDataUseCase
import com.agusteam.caribeando.domain.usecase.SignUpUseCase
import com.agusteam.caribeando.domain.usecase.StartStripeUseCase
import com.agusteam.caribeando.domain.usecase.UnmarkedFavoriteTripUseCase
import com.agusteam.caribeando.domain.validators.FieldValidator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val diDomainModule = module {

    single<FieldValidator> { FieldValidator() }
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<LoginUseCase> { LoginUseCase(get()) }
    single<RatingOrderUseCase> { RatingOrderUseCase(get()) }
    single<LogoutUseCase> { LogoutUseCase(get()) }
    single<RequestResetPasswordEmailUseCase> { RequestResetPasswordEmailUseCase(get()) }
    single<SignUpUseCase> { SignUpUseCase(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<TripProviderRepository> { TripProviderRepositoryImp(get()) }
    single<FillUserInfoUseCase> { FillUserInfoUseCase(get()) }
    single<GetCategoryUseCase> { GetCategoryUseCase(get()) }
    single<SaveTokenDataUseCase> { SaveTokenDataUseCase(get()) }
    single<GetLocalProfileUseCase> { GetLocalProfileUseCase(get()) }
    single<GetTripProviderDetailsUseCase> { GetTripProviderDetailsUseCase(get()) }
    single<LocalStoragePreferenceRepository> { LocalStorageDataStore(get()) }
    single<TripRepository> { TripRepositoryImp(get()) }
    single<UserProfileRepository> { UserProfileRepositoryImp(get()) }
    single<GetPaginatedTripsUseCase> { GetPaginatedTripsUseCase(get()) }
    single<UnmarkedFavoriteTripUseCase> { UnmarkedFavoriteTripUseCase(get()) }
    single<MarkFavoriteTripUseCase> { MarkFavoriteTripUseCase(get()) }
    single<GetTripsIncludedServicesUseCase> { GetTripsIncludedServicesUseCase(get()) }
    single<GetUpcomingTripByProviderUseCase> { GetUpcomingTripByProviderUseCase(get()) }
    single<GetTripFavoriteListUseCase> { GetTripFavoriteListUseCase(get()) }
    single<GetStripePaymentIntentUseCase> { GetStripePaymentIntentUseCase(get()) }
    single<CreatePendingPaymentOrderUseCase> { CreatePendingPaymentOrderUseCase(get()) }
    single<PaymentRepository> { PaymentRepositoryImp(get()) }
    single<StartStripeUseCase> { StartStripeUseCase(get()) }
    single<GoogleSignInUseCase> { GoogleSignInUseCase(get()) }
    single<ProcessSuccessPaymentOrderUseCase> { ProcessSuccessPaymentOrderUseCase(get()) }
    single<CancelPaymentOrderUseCase> { CancelPaymentOrderUseCase(get()) }
    single<GetUpcomingTripOrderUseCase> { GetUpcomingTripOrderUseCase(get()) }
    single<GetPastTripOrderUseCase> { GetPastTripOrderUseCase(get()) }
    single<OrderRepository> { OrderRepositoryImp(get()) }
    single<ReportOrderUseCase> { ReportOrderUseCase(get()) }
    single<GetUserProfileUseCase> { GetUserProfileUseCase(get()) }
    single<SaveLocalDataUseCase> { SaveLocalDataUseCase(get()) }
    /*  single<TokenRepository> {
          TokenRepositoryImp( get())
      }*/
}
