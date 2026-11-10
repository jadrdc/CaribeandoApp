repo: jadrdc/CaribeandoApp
branch: master
path: composeApp/src/commonMain

## Last sync
date: 2026-08-19T23:12:00Z

### Updated in this project
- Built `Caribeando App.dc.html` — a navigable redesign of the whole flow in direction 1a: login, signup, Explorar, filter sheet, trip detail, checkout, payment success, Viajes, review, Favoritos, Perfil, provider profile.
- Grounded in the repo's theme (#D62828 / #003049 / Poppins), Spanish strings from `strings.xml`, and the real vector drawables as inline SVG.
- Copied `brand/logo.png` and `brand/google.png` from `composeResources/drawable`.
- Added four screens the prototype was missing: Detalles de la reserva, Reportar inconveniente, ¿Olvidó su contraseña?, and the ErrorModal overlay.
- Designed the money paths that had no UI: Pagar faltante (balance), Pago fallido, and Cancelar reserva.
- Designed the five remaining gaps: galería de fotos, calendario de rango, cuenta sin confirmar, ErrorState a página completa, y Contáctanos.
- Added dark mode across every screen: each color carries a light and a dark value, switched by a `theme` prop.
- Added `Caribeando Screens.dc.html` — all 31 screens and states on one board, plus a dark-mode section.
- Earlier exploration of four Explorar directions kept in `Caribeando Redesign.dc.html`.

## Screen map
| Project screen | Repo files |
| --- | --- |
| Explorar | presenter/explore/screen/ExploreScreen.kt, explore/composable/TripItem.kt, CategoryItem.kt, CategorySection.kt, common/SearchBar.kt |
| Filter sheet | explore/composable/HomeFilterContent.kt, common/AmoutSliderPicker.kt, common/ModernDateRangePicker.kt, common/ChipFilter.kt |
| Trip detail | shopping/screen/ShoppingItemDetailScreen.kt, shopping/composable/*, common/CommentsSection.kt, common/CancellationPolicy.kt, common/MapDetails.kt, common/RatingAndReviews.kt |
| Checkout + success | shopping/screen/TripItemPayingScreen.kt, shopping/composable/ShoppingPaymentWay.kt, stripe/PaymentViewModel.kt |
| Viajes | orders/screen/OrderHistoryScreen.kt, orders/composable/UpcomingOrderTripItem.kt, PreviousTripItem.kt, common/EmptyState.kt |
| Review | orders/screen/ReviewOrderScreen.kt, orders/viewmodels/RatingOrderViewModel.kt |
| Favoritos | wishlist/screen/WishListScreen.kt, wishlist/composable/WishItem.kt, common/FavoriteButton.kt |
| Perfil | profile/screen/ProfileScreen.kt, profile/composable/ProfileHeader.kt, ProfileItem.kt |
| Provider profile | profile/screen/TripProviderProfileScreen.kt, common/ProviderProfileOverview.kt, ProviderStats.kt |
| Login / signup / recuperar | signup/screen/LoginScreen.kt, SignUpAccountScreen.kt, FillSignUpAccountScreen.kt, domain/usecase/RequestResetPasswordEmailUseCase.kt |
| Detalles de la reserva | orders/screen/OrderItemDetailsScreen.kt, orders/viewmodels/WishListOrderDetailViewModel.kt |
| Reportar inconveniente | orders/screen/ReportOrderIssueScreen.kt, shopping/state/ReportOrderState.kt, shopping/viewmodels/ReportOrderViewModel.kt |
| Error modal | common/ErrorModal.kt |
| Pagar faltante | data/network/services/PaymentService.kt, domain/models/PayItemModel.kt, shopping/composable/ShoppingPaymentWay.kt, values/strings.xml (booking_payment_description) |
| Pago fallido | data/network/services/PaymentService.kt (order-failure), data/model/PaymentFailureRequest.kt, domain/usecase/CancelPaymentOrderUseCase.kt |
| Cancelar reserva | domain/usecase/RemoveOrderUseCase.kt, data/network/services/PaymentService.kt (removeOrder) |
| Galería de fotos | shopping/composable/ShoppingitemHeader.kt, common/TripItemBar.kt |
| Calendario | common/ModernDateRangePicker.kt, common/ModernDatePicker.kt |
| Cuenta sin confirmar | data/model/Token.kt, data/imp/LoginRepositoryImpl.kt, home/screen/HomeScreen.kt |
| Error de página | common/ErrorState.kt, common/EmptyState.kt |
| Contáctanos | profile/screen/ProfileScreen.kt, values/strings.xml (contact_us, contact_provider) |
| Bottom nav | common/BottomNavigationBar.kt, home/model/BottomNavigationItem.kt |
| Theme + icons | presenter/theme/Colors.kt, Font.kt, composeResources/drawable/ic_*.xml, values/strings.xml |

## Dark mode
Colors.kt has no dark palette, so the dark values are new. Derived from the brand navy, not neutral grey.
| Role | Light (repo) | Dark (new) |
| --- | --- | --- |
| Page / frame | #FFFFFF | #101E28 |
| Ink | #003049 | #E8F1F6 |
| Navy as fill | #003049 | #3E6479 |
| Red as fill | #D62828 | #D62828 (unchanged) |
| Red as text/icon | #D62828 | #FF6E6E (contrast) |
| Secondary text | #5F6A70 | #9FB2BE |
| Body text | #3F4A52 | #C7D6DF |
| grey500 | #757575 | #8FA1AC |
| Field | #F4F5F6 | #182934 |
| Hairline | #EEF0F1 | #1E3039 |
| Border | #E2E2E2 | #263B47 |
| Red tint | #FDF4F4 | #2A1618 |
| Logo cream | #F6EDE2 | #F6EDE2 (unchanged) |
| Star | #FFB400 | #FFB400 (unchanged) |
