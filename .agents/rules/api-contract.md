# API Contract

The authoritative contract for every backend endpoint the client consumes. Read this before
planning or implementing any networked feature — never invent an endpoint, field name, or type.

## Source of truth

Caribeando has **no OpenAPI/Swagger spec** as of 2026-08-18 — `caribeando-backend` doesn't
generate one. Until it does (recommended: add `springdoc-openapi-starter-webmvc-ui` to
`caribeando-backend` so this stops being manual), the contract is:

**`caribeando-backend/src/main/kotlin/com/caribeando/app/{controllers,entities/dto,entities/request}/`**

For each endpoint a feature touches, read the actual controller method and its request/response
DTO classes — not this file (which will drift), not a description of the feature, not what a
similarly-named client-side model already assumes.

| Base path | Controller |
|---|---|
| `/auth` | `AuthController.kt` — login, signup, Google, Apple, refresh, phone/birthdate update |
| `/trip` | `TripController.kt` — search/browse, details, schedule, comments, favorites, categories, included services |
| `/provider` | `BusinessController.kt` — provider profile, upcoming trips, category assignment, business creation |
| `/order` | `OrderController.kt` — upcoming/past orders, rating, issue reporting, cancellation |
| `/payment` | `PaymentController.kt` — pending order creation, Stripe payment sheet, order success/failure |
| `/staff` | `StaffController.kt` — business staff roster CRUD |
| `/category` | `CategoryController.kt` |

## Rules

1. **Read the controller method signature and the DTO class it returns/accepts**, field by
   field, before writing or trusting a client-side model for that endpoint. Kotlin data class
   field names are case-sensitive and this backend mixes `camelCase` and `snake_case` fields
   *within the same DTO* (e.g. `TripDetailsDTO` has both `meeting_point` and `leavingTime`) — do
   not assume consistency, read the actual class.
2. **Cite what you read.** When recording a contract in a feature's notes/spec, name the exact
   file(s) read (e.g. "`TripController.getPaginatedTrips` + `TripPaginatedDTO` +
   `SummaryBusinessModelDTO` + `TripDetailsDTO`, read 2026-08-18") so it's clear whether the
   record might be stale.
3. **Reconcile on drift.** Backend DTOs change without warning here (no versioned spec yet) — if
   you're resuming work on a feature, re-read the relevant DTO/controller rather than trusting
   an old note.
4. **The client's own DTOs mirror the backend's, not the other way around.** If a client-side
   model (`data/model/*.kt`) disagrees with what the backend controller actually returns, the
   backend is right and the client model is the bug — unless you are deliberately changing the
   backend as part of the same piece of work.
5. **Flag genuine backend gaps.** If a screen needs a field the backend doesn't return (e.g. an
   explicit order-cancellation reason, a currency code), say so plainly and treat it as a
   backend change to make together — don't invent a client-side workaround that silently masks
   a missing field.
6. **Money and time fields need care.** Amounts (`initialPayment`, `totalPayment`, `amount`) are
   backend `Double`, not a currency-safe type, and there is no currency field anywhere in the
   contract yet (single implicit currency). Timestamps come back as `Instant`/`ZonedDateTime`
   serialized to ISO-8601 strings — don't assume a particular local timezone client-side.

## Known DTO shapes (as read 2026-08-18 — re-verify before relying on these for new work)

- `BusinessModelSummary` (`/provider/information/{id}`): id, name, image, description, phone,
  email, address, created_at, month, avatarUrl, categoryModel, offerTrips, activeTrips,
  **isVerified** (added 2026-08-18 — true when the business has an RNC on file; drives a
  "Verified Operator" badge client-side).
- `TripCommentResponse` (`/trip/comments/{tripId}`): rating, comment, name, image,
  **verifiedBooking** (added 2026-08-18, always `true` — a comment can only exist against a
  completed Order; drives a "Verified traveler" badge client-side).
- `TripPaginatedDTO` (`/trip/available`): rating, reviewCount, id, name, destiny, lat, lng,
  description, cancellation_policy, isFavorite, businessModel (`SummaryBusinessModelDTO`),
  details (`TripDetailsDTO`), images.

- `POST /order/{id}/cancel` (added 2026-08-18, `OrderController.cancelOrder`): traveler-initiated
  cancellation of an already-placed order (distinct from `/payment/order-failure`, which is for a
  payment attempt that never succeeded — no charge exists yet, nothing to refund). Request body
  `CancelOrderRequest { reason: String? }`. Response `CancelOrderResponse { status: String,
  refundEligible: Boolean, message: String }`. Rules enforced server-side: (1) the order must
  belong to the calling user (ownership check against the JWT subject); (2) only orders whose
  `status` is `pending` or `success` can be cancelled — anything already
  cancelled/refunded/completed is rejected; (3) refund eligibility is a 48-hour cutoff computed
  from `Duration.between(now, tripScheduled.leavingTime).toHours()` — inside 48h of departure,
  `refundEligible = false` and status becomes `cancelled_by_traveler`; outside it,
  `refundEligible = true` and status becomes `refund_pending`; (4) the trip's scheduled slot is
  released back to inventory; (5) an audit row is written via the existing
  `OrderFailureRepository`/`OrderFailure` entity rather than a new table.
  **Known gap, deliberate:** this endpoint does NOT call Stripe yet — `PaymentService.kt` was an
  unreadable iCloud placeholder when this was implemented (2026-08-18), so `refund_pending` orders
  require a manual operator-side refund until that file syncs and the real `Refund.create()` call
  is wired in. `Order.status` stays a plain `String?` column (see `OrderStatus.kt` enum for the
  known values/`fromValue()` mapping) rather than a JPA-converted enum, specifically to avoid
  needing to touch the also-unreadable `OrderRepository.kt`.
- `OrderDTO` gained an explicit `orderId: String` field (added 2026-08-18) — the pre-existing `id`
  field is actually populated with `order.stripeTransaction` (the Stripe charge id), not the
  database UUID, so before this the client had no way to address a specific order for actions
  like cancellation. Use `orderId` for anything that calls back into `/order/{id}/...`; treat `id`
  as the Stripe transaction reference only.

Everything else referenced in code (`GetPaginatedTripRequest`, `CreateOrderRequest`,
`PaymentRequest`, and others) was inferred from controller *usage* rather than the request DTO's
own source at scaffolding time — treat those specifically as unverified until you've opened the
actual `entities/request/*.kt` file.
