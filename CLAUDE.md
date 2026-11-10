# Project Rules — Caribeando (KMP client)

## Overview

Caribeando is a Kotlin Multiplatform mobile marketplace (Android + iOS, Compose Multiplatform
UI) connecting Dominican Republic / Caribbean tour operators ("Business" providers) with
travelers booking excursions ("Trips"). Single module: `:composeApp`, with `commonMain` holding
nearly all logic and thin `androidMain` / `iosMain` / `appleMain` shims for native concerns
(Google/Apple Sign-In, Stripe's native SDK, maps, crash reporting).

It talks to `caribeando-backend` (sibling repo, Kotlin/Spring Boot) — see that repo's own
CLAUDE.md for the server side.

> This rule set was scaffolded on 2026-08-18 by reverse-engineering the codebase (entities,
> controllers, screen/viewmodel structure). Most of `composeApp/` was initially unreadable
> (stuck as un-downloaded iCloud placeholders); DI and networking were confirmed shortly after
> against the real `gradle/libs.versions.toml` and `composeApp/build.gradle.kts` — see below.

---

## Architecture Layers

Confirmed from `composeApp/src/commonMain/kotlin/.../caribeando/{data,domain,presenter}/`:

- **`domain/`** — `models/` (pure Kotlin domain models), `usecase/` (one class per business
  operation, e.g. `GetTripDetailsUseCase`, `LoginUseCase`, `CreatePendingPaymentOrderUseCase`),
  `interfaces/` (repository interfaces, e.g. `TripRepository`, `OrderRepository`),
  `validators/` (form validation rules).
- **`data/`** — `model/` (network DTOs, e.g. `TripModel`, `BusinessProfileModel`), `imp/`
  (repository implementations, e.g. `TripRepositoryImp`), `network/services/` (per-domain API
  service classes, e.g. `TripService`, `OrderService`, mirroring `caribeando-backend`'s
  controllers 1:1 — see `.agents/rules/api-contract.md`), `network/createHttpClient.kt`,
  `mappers/` (DTO ↔ domain model mapping), `database/` (local key-value storage), `util/`.
- **`presenter/`** — one package per feature (`home`, `explore`, `signup`, `shopping`,
  `orders`, `wishlist`, `profile`, `stripe`, `common`), each with `screen/`, `viewmodel(s)/`,
  `state/`, `composable/`, `navigation/`. `presenter/common/` holds cross-feature reusable
  composables (buttons, badges, loading states, map/search components).
- **`core/`** — `base/` (`GenericViewModel`, `OperationResult`, `NetworkError`, `Error`
  sealed types shared across the app), `di/` (see Dependency Injection below).

Dependency direction: `presenter → domain ← data`. `domain` must not import platform
(`android.*`/`platform.*`) or Ktor/network types directly — only through its repository
interfaces. `data` implements those interfaces; `presenter` never calls `data` directly.

## Patterns & Conventions

Naming (confirmed from existing files):

| Element | Convention | Example |
|---|---|---|
| Use case | `{Action}{Entity}UseCase` | `GetPaginatedTripsUseCase`, `MarkFavoriteTripUseCase` |
| Repository interface | `{Entity}Repository` | `TripRepository`, `TripProviderRepository` |
| Repository implementation | `{Entity}RepositoryImp` (note: **`Imp`, not `Impl`** — match existing spelling, don't "fix" it mid-feature) | `TripRepositoryImp` |
| ViewModel | `{Feature}ViewModel` | `ExploreViewModel`, `ProfileViewModel` |
| Screen composable | `{Feature}Screen` | `HomeScreen`, `TripProviderProfileScreen` |
| API service | `{Entity}Service` | `TripService`, `PaymentService` |
| Network DTO | `{Entity}Model` / `{Entity}Request` / `{Entity}Response` | `TripModel`, `LoginRequest`, `UserSignUpResponse` |

All ViewModels extend the shared `GenericViewModel` base and expose results via
`OperationResult<T>` (see `core/base/OperationResult.kt`) — never let a use case throw for an
expected failure (bad login, empty cart, network error); wrap it.

## Dependency Injection — **Koin (confirmed)**

Confirmed from `gradle/libs.versions.toml`: `koin-core`, `koin-android`, `koin-compose`,
`koin-androidx-compose`, `koin-compose-viewmodel` (Koin 4.2.2). `core/di/DIData.kt`,
`DIDomain.kt`, `DIViewModels.kt` (plus per-platform `DIData.android.kt` / `DIData.ios.kt`) are
Koin modules. Add new injectable dependencies as Koin module bindings in the matching
`DI*.kt` file rather than hand-wiring constructors.

## Networking — **Ktor Client (confirmed)**

Confirmed from `gradle/libs.versions.toml`: `ktor-client-core`, `ktor-client-okhttp` (Android
engine), `ktor-client-darwin` (iOS engine), `ktor-client-auth`, `ktor-client-logging`,
`ktor-client-content-negotiation`, `ktor-client-encoding`, `ktor-serialization-kotlinx-json`
(Ktor 3.5.2). `data/network/createHttpClient.kt` builds the shared `HttpClient` with these
plugins. Use the content-negotiation + kotlinx-serialization-json setup already configured
there for any new endpoint — don't hand-roll a second client or a manual interceptor.

For the authoritative request/response shapes, see `.agents/rules/api-contract.md` — **the
backend Kotlin source is the contract**, not this file, not a ticket, not a guess.

## Compose

Compose Multiplatform, used directly in `commonMain` (not behind expect/actual — e.g.
`BottomModalSheet.kt`, `PullToRefreshContainer.kt` live in `presenter/common/` as plain
composables). Evidence points to **Material3** (`PullToRefreshContainer` is an M3-only API).
Prefer `presenter/common/` for any composable used by more than one feature; keep
feature-specific composables inside that feature's own `presenter/{feature}/composable/`
package rather than promoting them to `common` prematurely.

## Payments, Auth, Storage

- **Stripe** — native PaymentSheet per platform (`StripeButton.android.kt` / `.ios.kt`,
  `StripeNativeBridge.kt` on iOS), driven from `presenter/stripe/PaymentViewModel.kt`.
- **Auth** — email/password, Google Sign-In, Sign in with Apple; JWT access+refresh pair,
  persisted via `data/database/createDatabasePreference` (platform-specific local storage).
- **Images** — uploaded to Firebase Storage by the backend; client only uploads/displays URLs.

## Build Configuration

- Root `build.gradle.kts` aliases plugins via the version catalog:
  `androidApplication`, `androidLibrary`, `compose.compiler`, `jetbrainsCompose`,
  `kotlinMultiplatform`, `jetbrains.kotlin.serialization`, `ksp`, plus
  `com.google.gms.google-services` and `com.google.firebase.crashlytics` applied directly.
  Only `:composeApp` is registered in `settings.gradle.kts`.
- **`gradle/libs.versions.toml` is the version source of truth** — never hardcode a dependency
  version in a module build file. This file was unreadable at scaffolding time (see the note at
  the top of this doc); read it before any version-bump work — see the pending KMP upgrade task.
- Targets: Android, iOS (`iosApp/` — SwiftUI entry point wrapping the shared Compose UI),
  Desktop (per `README.md`; no `desktopMain` source files were found, so the desktop target may
  be declared but not actively built out — confirm before doing desktop-specific work).

## Git Workflow — **not yet applicable**

No `.git` directory exists in this project as of 2026-08-18 — it's not under version control
yet. Before using branch/commit conventions from `.claude/skills/commit-hygiene/SKILL.md`, the
repo needs `git init` (or cloning from a remote if one already exists elsewhere) and a first
commit establishing a baseline.

## Testing

`composeApp/src/commonTest/kotlin/.../ComposeAppCommonTest.kt` exists but its content wasn't
readable at scaffolding time — treat "what testing framework/conventions are actually in use"
as unconfirmed. No CI, no lint (ktlint/detekt), no E2E framework (Maestro or otherwise) were
found configured. These are real gaps relative to a mature setup like Koombea's Cineco apps —
see `.agents/rules/ci-build-config.md`.

## Quick Reference — Do's and Don'ts

**Do:**
- Follow `presenter → domain ← data` strictly; don't let `presenter` reach into `data` directly.
- Wrap expected failures in `OperationResult.Error`, never throw.
- Match existing naming exactly (`RepositoryImp`, not `RepositoryImpl`) rather than "correcting" it mid-feature.
- Resolve request/response shapes against the actual `caribeando-backend` controller/DTO source, not assumption.
- Fix small bad practices in code you're already touching.

**Don't:**
- Hardcode dependency versions, base URLs, or secrets — check `Constants.kt` / the version catalog first (both were unreadable at scaffolding time; read them before assuming a pattern).
- Invent an endpoint or response field — read the backend controller.
- Silently rename `Imp` → `Impl` or otherwise "clean up" naming outside the scope of the feature you're building.
- Commit `google-services.json`, `secrets.properties`, `local.properties`, or any `.jks` keystore.
