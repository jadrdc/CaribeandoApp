# Handoff: Caribeando UI redesign

## Overview

A full redesign of the Caribeando customer app — a Spanish-language marketplace for
excursions in the Dominican Republic. 31 screens and states covering login, discovery,
booking, payment, trip management, reviews and account, in light and dark themes.

The redesign keeps the existing product structure and the existing brand colors and
typeface. What changes is the visual system: flatter surfaces, a stricter type scale,
red reserved for money and primary actions, and consistent empty/loading/error states.
It also adds screens for flows the codebase implies but never rendered — paying a
booking balance, a failed payment, cancelling a booking, and four others (see
[New screens](#new-screens)).

## About the design files

**The files in this bundle are design references, not production code.** They are HTML
prototypes that show intended layout, color, typography, copy and behavior. Do not port
the HTML or its markup structure.

The target is the existing repository: **`jadrdc/CaribeandoApp`**, a Kotlin Multiplatform
app with a Compose Multiplatform UI shared across Android and iOS. Implement these designs
as Compose composables in `composeApp/src/commonMain/kotlin/com/agusteam/caribeando/`,
reusing the existing structure:

- Screens live in `presenter/<feature>/screen/`
- Reusable pieces in `presenter/common/` and `presenter/<feature>/composable/`
- State in `presenter/<feature>/state/`, ViewModels in `presenter/<feature>/viewmodels/`
- Theme in `presenter/theme/Colors.kt` and `Font.kt`
- Icons are already in `composeResources/drawable/` as vector XML
- Strings are already in `composeResources/values/strings.xml`

Reuse and restyle the existing composables (`ActionButton`, `SearchBar`, `TripItem`,
`EmptyState`, `ErrorModal`, `BottomNavigationBar`, `CustomRadioButton`, `PullToRefreshContainer`,
`CommentsSection`, `RatingAndReviews`, `ItemProviderOverviewItem`, …) rather than
introducing parallel ones. The section [Repo file map](#repo-file-map) says which existing
files each screen corresponds to.

### How to open the prototypes

`Caribeando App.dc.html` and `Caribeando Screens.dc.html` need `support.js` sitting next to
them (included). Open either file directly in a browser.

- **`Caribeando App.dc.html`** — the clickable prototype. Everything is tappable.
- **`Caribeando Screens.dc.html`** — all 31 screens side by side, grouped by flow, plus a
  dark-mode section. Best way to see the whole system at once.
- **`Caribeando Redesign.dc.html`** — the four original direction explorations. Direction
  **1a** was chosen; the other three are context only, do not implement them.

## Fidelity

**High fidelity.** Colors, typography, spacing, radii, copy and transitions are final and
exact. Recreate them precisely. Two caveats:

1. **All imagery is placeholder.** Every photo is a flat gradient block labelled
   `foto · <name>`. Wire real images from the existing `AsyncImage`/Coil setup. Aspect
   ratios and corner radii in this document are the spec; the gradients are not.
2. **All content is invented** — trip names, prices, provider names, review text, the user's
   name. Use real data. Copy that came from `strings.xml` is marked as such and is verbatim.

---

## Design tokens

### Color — light

Existing values from `presenter/theme/Colors.kt` are marked ✓. Everything else is new.

| Role | Value | Notes |
| --- | --- | --- |
| `primary` | `#D62828` ✓ | Money, primary buttons, active nav, destructive |
| `secondary` | `#003049` ✓ | Primary text, filled chips, dark UI surfaces |
| `background` | `#FFFFFF` ✓ | Page and card surface |
| `grey500` | `#757575` ✓ | Review counts only |
| `greyColor` | `#E2E2E2` ✓ | Dividers, unselected radio, empty stars |
| Text secondary | `#5F6A70` | Metadata, labels, helper text. Replaces uses of grey500 |
| Text body | `#3F4A52` | Multi-line descriptions and review bodies |
| Icon inactive | `#8A9298` | Chevrons, inactive nav icons |
| Field fill | `#F4F5F6` | Inputs, search bar, back buttons |
| Hairline | `#EEF0F1` | Top/bottom bar borders |
| Panel border | `#EDEFF1` | Card and list-group borders |
| Row divider | `#F2F4F5` | Dividers inside list groups |
| Red tint | `#FDF4F4` | Selected payment card, balance-due panel |
| Cream | `#F6EDE2` | Logo plate, avatar plate, status pill |
| Dashed border | `#CFD5DA` | Confirmation-code block, disabled day numbers |
| Subtle fill | `#FBFBFC` | Total row in the balance breakdown |
| Avatar placeholder | `#E7EBEE` | |
| Star | `#FFB400` ✓ | From `RatingAndReviews.kt` |
| Disabled button | `#EDB4B4` | `primary` at ~50%, matches `ActionButton(isValid=false)` |
| Sheet grabber | `#DDE2E6` | |
| Spinner track | `#F0DCDC` | |
| Scrim | `rgba(0,48,73,.34)` | Behind sheets and modals |
| Overlay on photo | `rgba(255,255,255,.94)` | Circular back/heart buttons |
| Sticky bar | `rgba(255,255,255,.97)` | Bottom action bars |
| Photo placeholder | `linear-gradient(160deg,#E9EDF0,#DCE3E8)` | Replace with real images |
| Shimmer | `#F1F3F4` → `#E6E9EB` → `#F1F3F4` | Matches existing `shimmerEffect` |
| Map water / grid / land | `#E8EEF1` / `#DBE4E9` / `#D7E1E7` | Schematic only — real map is `MapComponent` |

### Color — dark

`Colors.kt` has no dark palette. All of these are new, derived from the brand navy rather
than neutral grey so the dark theme reads as the same brand.

| Role | Light | Dark |
| --- | --- | --- |
| Page / card surface | `#FFFFFF` | `#101E28` |
| Primary text | `#003049` | `#E8F1F6` |
| Navy as a **fill** | `#003049` | `#3E6479` |
| Red as a **fill** | `#D62828` | `#D62828` — unchanged |
| Red as **text or icon** | `#D62828` | `#FF6E6E` |
| Text secondary | `#5F6A70` | `#9FB2BE` |
| Text body | `#3F4A52` | `#C7D6DF` |
| grey500 | `#757575` | `#8FA1AC` |
| Icon inactive | `#8A9298` | `#7E9099` |
| Field fill | `#F4F5F6` | `#182934` |
| Hairline | `#EEF0F1` | `#1E3039` |
| Panel border | `#EDEFF1` | `#22353F` |
| Row divider | `#F2F4F5` | `#1C2E38` |
| Red tint | `#FDF4F4` | `#2A1618` |
| Dashed border | `#CFD5DA` | `#3A4F5C` |
| Disabled text | `#CFD5DA` | `#5A6E7A` |
| Subtle fill | `#FBFBFC` | `#0D1A23` |
| Avatar placeholder | `#E7EBEE` | `#223440` |
| Empty star | `#E2E2E2` | `#2C4050` |
| Divider fill | `#E2E2E2` | `#22343F` |
| Panel/field border | `#E2E2E2` | `#263B47` |
| Disabled button | `#EDB4B4` | `#5E2A2A` |
| Sheet grabber | `#DDE2E6` | `#344854` |
| Spinner track | `#F0DCDC` | `#3A2426` |
| Scrim | `rgba(0,48,73,.34)` | `rgba(0,0,0,.62)` |
| Overlay on photo | `rgba(255,255,255,.94)` | `rgba(12,26,34,.88)` |
| Sticky bar | `rgba(255,255,255,.97)` | `rgba(16,30,40,.97)` |
| Photo placeholder | `#E9EDF0`→`#DCE3E8` | `#1B2B35`→`#16232C` |
| Shimmer | `#F1F3F4`/`#E6E9EB` | `#17262F`/`#1E303B` |
| Map water / grid / land | `#E8EEF1`/`#DBE4E9`/`#D7E1E7` | `#132430`/`#1B2E3A`/`#1E3441` |
| Cream plate | `#F6EDE2` | `#F6EDE2` — unchanged |
| Star | `#FFB400` | `#FFB400` — unchanged |

**Three decisions worth understanding before you implement this:**

1. **Red splits by role in dark.** `#D62828` on a dark ground measures ~3.3:1, which fails
   WCAG AA for body text. As a *fill* with white text it passes, so filled buttons keep the
   exact brand red; as text or an icon it lightens to `#FF6E6E` (~6.4:1). Model this as two
   theme slots, e.g. `primary` and `onSurfaceDanger` — not one color.
2. **Navy also splits by role.** As text it inverts to near-white; where it was a *fill*
   (active category chip, filter button, date chip) it lifts to `#3E6479` so it still reads
   as a filled surface against `#101E28`.
3. **The cream plate never changes.** `logo.png` is a raster with its own cream background,
   so a dark plate would frame a cream square. The red icons that sit on cream stay
   `#D62828` in both themes for the same reason.

The gallery screen is dark in **both** themes by design (`#0B1B24` ground) — photo viewers
are conventionally dark. Do not theme it.

### Typography

`Poppins`, already wired in `presenter/theme/Font.kt`. Weights used: 400 Regular,
500 Medium, 600 SemiBold. The prototype uses no italics and no other family.

| Role | Size / line-height | Weight | Tracking |
| --- | --- | --- | --- |
| Screen title (tab roots) | 26 / 1.15 | 600 | −0.02em |
| Screen title (pushed screens) | 25 / 1.25 | 600 | −0.02em |
| Screen title (compact) | 21–22 / 1.25 | 600 | −0.02em |
| Success / failure headline | 24–26 / 1.25 | 600 | −0.02em |
| Balance hero amount | 34 / 1.1 | 600 | −0.02em |
| Nav bar title | 17 | 600 | — |
| Section header | 15 | 600 | — |
| Feed card title | 16 / 1.3 | 600 | — |
| List row title | 13.5–14 / 1.3 | 600 | — |
| Price — feed | 17 | 600 | — |
| Price — checkout total | 20 | 600 | — |
| Body / description | 13 / 1.65 | 400 | — |
| Metadata | 12.5 | 400 | — |
| Metadata small | 11–11.5 | 400 | — |
| Field label (caps) | 10 | 500 | 0.10em, uppercase |
| Eyebrow (caps) | 9.5 | 500 | 0.16em, uppercase |
| Primary button | 15 | 600 | — |
| Sheet button | 14.5 | 600 | — |
| Pill button | 11.5 | 600 | — |
| Chip | 12.5 | 500 | — |
| Tab — active / inactive | 13.5 | 600 / 400 | — |
| Bottom nav label | 10 | 500 active / 400 inactive | — |
| Rating value | 12–12.5 | 600 | — |

### Spacing, radii, sizing

- **Screen gutter: 20dp.** The current app uses 16dp — this is an intentional change; apply
  it everywhere except full-bleed images and list rows inside cards.
- Vertical rhythm: 26dp between major sections, 12–14dp between a header and its content,
  28dp between feed cards, 12dp between list rows, 16dp between stacked cards.
- Radii: `38` phone frame (prototype chrome only), `26` sheet top corners, `22` large tile,
  `18` card / photo / modal, `16` panel / row / textarea, `14` button / input, `12` small
  control, `999` pill and circle.
- Control heights: primary button 48 (15dp padding), input 46, search bar 46, chip 34,
  pill button 32, back button 38×38, circular photo button 34–36.
- Icons: 22 bottom nav, 19 inline and detail rows, 17–18 search / heart / chevron-adjacent,
  16 chevron, 15 chip, 13–14 inline star, 42 rating star, 46 failure mark, 100 full-page
  error mark.
- Bottom bar: 14dp top padding, 18dp bottom padding, 1dp hairline top border. Bottom nav:
  12dp top, 26dp bottom.
- Feed photo: full width × 216, radius 18. Detail hero: full-bleed × 300. Order hero:
  full-bleed × 210. Row thumb: 88×88 or 96×110, radius 11–12. Provider cover: 150.

**Known deviation to fix in implementation:** category chips and pill buttons are 32–34dp
tall, below the 44dp minimum touch target. Keep the visual size and expand the touch area
with padding or `Modifier.minimumInteractiveComponentSize()`.

---

## Shared components

Specify these once and reuse them; the screen list below refers to them by name.

### PrimaryButton
Full width, `#D62828` fill, radius 14, 15dp padding, label 600/15 white, centered.
Disabled: fill `#EDB4B4`, same label. Existing `ActionButton` — change radius 8 → 14.

### SecondaryButton
Full width, transparent fill, 1dp `#E2E2E2` border, radius 14, 14dp padding, label 600/14
in `#003049`.

### TextLink
Centered label 600/13 in `#D62828`. The muted variant is 500/12.5 in `#5F6A70` with
underline (used for destructive secondary actions like *Cancelar reserva*).

### Field
Fill `#F4F5F6`, radius 14, padding 11 × 15. Caps label 500/10 `#5F6A70` above the value;
value 400/14 `#003049`, 3dp below the label. No border, no focus ring; focus is the caret.

### SearchBar
Height 46, fill `#F4F5F6`, radius 14, 14dp horizontal padding, 9dp gap. `ic_search` at 17
in `#5F6A70`, then placeholder 400/13 `#5F6A70`. Trailing 46×46 square, radius 14, fill
`#003049`, `ic_filter` at 19 in white. The whole bar opens the filter sheet — matching
today's `SearchBar` behavior. The search *input* itself lives inside the sheet.

### Chip
Radius 999, padding 8 × 14, 6dp gap, icon 15, label 500/12.5.
Unselected: 1dp `#E2E2E2` border, `#003049` content. Selected: `#003049` fill, white
content. Category chips carry their real drawable: `ic_beach`, `ic_mountain`, `ic_hiking`,
`ic_camping`; *Todo* uses `ic_search`.

### ListGroup
Rounded container, radius 16, 1dp `#EDEFF1` border, `clipToBounds`. Rows are 14–15dp
padding; between rows a 1dp `#F2F4F5` divider, full bleed inside the container.
Label–value row: label 400/12 `#5F6A70` left, value 500/13 `#003049` right.
Navigation row: leading icon 19 `#003049`, label 500/13, trailing `chevron_right_small` 16
in `#8A9298`.

### TripCard (feed)
Photo full width × 216, radius 18. Heart: 34×34 circle, `rgba(255,255,255,.94)`, inset 12
from top-right; `ic_favorite_filled` 18 `#D62828` when saved, `ic_favorite` 18 `#003049`
when not. Below the photo, 12dp down: title 600/16 `#003049` left, rating right
(`ic_star` 13 `#FFB400` + value 600/12 `#003049` + count 400/12 `#757575`). 3dp down:
`destino · fecha · duración` 400/12.5 `#5F6A70`. 12dp down: price 600/17 `#D62828` +
`por persona` 400/11.5 `#5F6A70` on the left; 20×20 provider avatar + name 400/11.5
`#5F6A70` on the right.

### TripRow (compact)
Card radius 16, 1dp `#EDEFF1` border, 12dp padding, 12dp gap. Thumb 88×88 (or 66×66 in
Viajes, 72×72 on the provider profile), radius 12. Right column: title 600/13.5, meta
400/11 `#5F6A70`, optional rating line, price 600/14 `#D62828`.

### BottomSheet
Fills width, top radius 26, `#FFFFFF`, max height 88% of the screen. 40×4 grabber, radius
2, `#DDE2E6`, centered, 10dp from the top. Scrolling body, then a fixed footer with a 1dp
`#EEF0F1` top border, 14dp top / 22dp bottom padding. Scrim `rgba(0,48,73,.34)`, tap to
dismiss.

### BottomNav
Four items — Explorar, Favoritos, Viajes, Perfil — evenly spaced, 1dp `#EEF0F1` top border,
12dp top / 26dp bottom padding. Icon 22–23 above a 10pt label, 5dp gap. Active `#D62828`
with a 500 label; inactive `#8A9298` with a 400 label.
Icons are the existing drawables and **are not what you might assume**: Explorar is
`ic_explore` (a magnifier, identical to `ic_search`), Favoritos is `ic_wish_list` (a list,
not a heart), Viajes is `ic_trip`, Perfil is `ic_profile`. Keep them.
Visible only on the four tab roots; hidden on every pushed screen, as `HomeScreen` already
does via `HomeOption.SHOPPING_ITEM_DETAIL`.

### EmptyState
Centered column, 60dp from the top of the content area. 78×78 cream tile radius 22 holding
the logo at 58×58, radius 16, 55% opacity. 20dp down: title 600/16 `#003049`. 6dp down:
one line of 400/12.5 `#5F6A70`. 18dp down: a TextLink action.
This replaces `empty_state.png` (1.4 MB) in the current `EmptyState`.

### Toast
Pinned 26dp from the bottom, 20dp side insets. `#003049` fill, radius 14, padding 13 × 16,
label 500/12.5 white, centered. Auto-dismisses after 2200ms.

---

## Screens

Grouped as they appear in `Caribeando Screens.dc.html`. **[NEW]** marks a screen with no
current implementation.

### Entrada

**1. Iniciar sesión** — `presenter/signup/screen/LoginScreen.kt`
62×62 cream tile radius 18 with the logo at 48×48, 28dp from the top. 26dp down: title
`Iniciar sesión`. 6dp: `Excursiones en República Dominicana, reservadas en dos toques.`
28dp: two Fields (Correo electrónico, Contraseña — the password field carries a trailing
`Mostrar` label, 500/11.5 `#003049`). 20dp: PrimaryButton `Iniciar sesión`. 16dp: centered
TextLink `¿Olvidó su contraseña?`. 24dp: a rule–`o`–rule divider (1dp `#E2E2E2`, 12dp gaps,
`o` at 400/11 `#5F6A70`). Then SecondaryButton with `google.png` at 18 and
`Continue with Google` (verbatim from `strings.xml`). Pinned to the bottom: `¿Primera vez?`
400/12.5 `#5F6A70` with `Unirse a la cuenta` in 600 `#D62828`.

**2. Completa tu cuenta** — `signup/screen/SignUpAccountScreen.kt`
38×38 back button (radius 12, `#F4F5F6`, `ic_arrow_back` 19). 20dp: title
`Completa tu cuenta`. 22dp: Nombre + Apellido side by side (11dp gap), then Correo
electrónico, Teléfono, Contraseña, Confirmar contraseña. On mismatch, 12dp below the last
field: `cancel` icon 15 `#D62828` + `Debes usar la misma contraseña para ambos campos`
(verbatim) at 400/11.5 `#D62828`. 18dp: the terms paragraph at 400/10.5/1.6 `#5F6A70`,
verbatim from `strings.xml`. 16dp: PrimaryButton `Completar cuenta`.

**3. ¿Olvidó su contraseña?** **[NEW]** — backend already exists
(`RequestResetPasswordEmailUseCase`)
Back button, 24dp, title, 8dp: `Escribe el correo de tu cuenta y te enviamos un enlace para
crear una nueva.` 24dp: Correo Field. 20dp: PrimaryButton `Enviar enlace`. Pinned bottom:
`Volver a iniciar sesión` 500/12.5 `#003049`. Submitting returns to login with the toast
`Te enviamos un enlace a tu correo`.

**4. Cuenta sin confirmar** **[NEW]** — replaces the silent logout
Today `HomeScreen` calls `logout()` whenever `Token.isConfirmed` is false, which dead-ends
the user. Instead: centered 96×96 cream tile radius 28 with the logo at 72×72, headline
`Confirma tu correo`, then `Te enviamos un enlace a <correo>. Ábrelo para poder reservar
excursiones.` with the address in 600 `#003049`. 22dp: a `#F4F5F6` panel radius 14 with
`Puedes seguir explorando mientras tanto, pero el pago queda bloqueado hasta que confirmes.`
Bottom: PrimaryButton `Reenviar correo`, SecondaryButton `Seguir explorando`, muted link
`Cerrar sesión`.

### Explorar

**5. Explorar — cargando** — `explore/screen/ExploreScreen.kt`, skeleton per
`common/loading/TripItemLoading.kt`
Header, search bar and chips render immediately; only the feed is skeletonised. Two card
skeletons: 216-tall shimmer block radius 18, then a 14-tall bar at 62% width radius 7
(14dp down), then a 12-tall bar at 40% width radius 6 (9dp down). Existing `shimmerEffect`,
1.3s linear loop.

**6. Explorar** — `explore/screen/ExploreScreen.kt` + `explore/composable/TripItem.kt`
Header: title `Explorar` with `N excursiones en República Dominicana` 400/12 `#5F6A70`
beneath; right, a 38×38 cream circle with `ic_profile` 20 `#D62828` that opens Perfil.
18dp: SearchBar. 16dp: horizontally scrolling Chip row (Todo, Playa, Montaña, Senderismo,
Camping). 22dp: the feed, TripCard with 28dp gaps, keeping the existing pagination and
pull-to-refresh. Tapping a card pushes the detail; tapping the heart toggles favourite
without navigating.

**7. Filtrar** — `explore/composable/HomeFilterContent.kt`
BottomSheet. Header row: `Filtrar` 600/18 left, `cancel` icon 22 `#8A9298` right. 18dp: the
search input (SearchBar shell, real text input, placeholder `¿Cuál es tu próxima aventura?`
verbatim). 24dp: `Tipos de excursión` (verbatim) + wrapping Chip row. 24dp: `Monto máximo` +
chips `Cualquiera`, `Hasta RD$2,000`, `Hasta RD$4,000`, `Hasta RD$6,000`. 24dp:
`¿Cuándo quisieras viajar?` with `Fechas exactas` 600/12 `#D62828` on the right of the
header, then chips `Cualquier fecha`, plus concrete upcoming dates; when a range is set it
appears as a leading selected chip. Footer: `Borrar filtros` (verbatim, 600/13 `#003049`,
underlined) and a PrimaryButton reading `Ver N resultados` — singular `Ver 1 resultado` —
falling back to `Filtrar` when nothing is filtered.
This replaces `AmountSliderPicker` with budget chips and moves the calendar to its own
screen. Both filter live: the button label updates as you change chips.

**8. Calendario** **[NEW screen, existing logic]** — replaces inline
`ModernDateRangePicker`
Fixed header: back button + `¿Cuándo quisieras viajar?` 600/17. 16dp: the range label
600/20 (`22 ago – 23 ago`, or `Elige tus fechas`). 3dp: a hint that changes with state —
`Toca el día de salida` → `Ahora elige el día de regreso` → `Toca cualquier día para empezar
de nuevo`. 16dp: weekday row `D L M M J V S`, 500/10.5 `#5F6A70`, 7 equal columns — **week
starts Sunday**, matching the existing picker. Below, a scrolling body of month blocks:
month name capitalised 600/14, then a 7-column grid, 2dp gaps, square cells.
Cell states: past → 400/13 `#CFD5DA`, not tappable. Available → 400/13 `#003049`. Range
end-points → `#003049` circle, 600/13 white. In between → `#EAF0F3` circle, 500/13
`#003049`. Footer: `Borrar` underlined + PrimaryButton `Aplicar fechas`.
Selection follows the existing logic in `ModernDateRangePicker`: first tap sets the start
and clears the end; a tap before the start swaps them; tapping the start clears the end;
otherwise it sets the end.

**9. Explorar — sin resultados**
EmptyState with `Nada por aquí todavía`, `Ningún viaje coincide con esos filtros.` and the
action `Borrar filtros`.

**10. Error (modal)** — `common/ErrorModal.kt`
Scrim, then a centered card: full width minus 26dp insets, `#FFFFFF`, radius 18, 20dp
padding. `cancel` icon 22 `#D62828` + title 600/16 `#003049` in a row, 12dp down the message
at 400/12.5/1.6 `#5F6A70`, 20dp down a PrimaryButton `Ok` (verbatim) at radius 12.

**11. Error de página** — `common/ErrorState.kt`
Full-screen centered: `cancel` icon at 100 in `#D62828`, 24dp down
`Algo salió mal. Por favor, inténtalo de nuevo más tarde.` at 500/18/1.45 `#003049`
(verbatim), 12dp down `Si sigue pasando, escríbenos y lo revisamos contigo.` Bottom:
PrimaryButton `Intentar de nuevo` and a TextLink `Contáctanos`.

### Reservar

**12. Detalles del viaje** — `shopping/screen/ShoppingItemDetailScreen.kt`
Full-bleed hero 300 tall (the existing `HorizontalPager`). Overlaid: 36×36 back circle top
left, 36×36 heart top right, and a page counter bottom right — pill,
`rgba(255,255,255,.94)`, padding 5 × 10, 500/10 `#003049`, reading `1 / 6`; tapping it opens
the gallery. Note this replaces today's black `8dp`-radius counter.
Body at 20dp gutter: title 600/22/1.25. 8dp: `ic_star` 14 `#FFB400` + rating 600/12.5 +
`(N reseñas)` 400/12.5 `#757575`. 14dp: description 400/13/1.65 `#3F4A52`. 8dp:
`Leer más` 600/12.5 `#D62828` (existing `ReadMoreText`).
22dp: the provider row — 1dp `#EDEFF1` panel radius 16, 13dp padding, 42×42 avatar circle,
name 600/13.5, `Agencia · 4.9 (127)` 400/11.5 `#5F6A70`, chevron 16. Pushes the provider
profile.
24dp: a two-column detail grid, 16dp gaps. Each cell: icon 19 `#003049` + a label 400/10.5
`#5F6A70` over a value 500/12.5. In order — Destino (`ic_pin`), Lugar de salida
(`ic_address`), Hora de salida (`ic_clock`), Hora de llegada (`ic_clock`), Pago de reserva
(`ic_cash`), Pago total (`ic_cash`). Labels are verbatim from `strings.xml`.
24dp: the map at 150 tall, radius 16 — use the real `MapComponent`/`MapDetails`.
26dp: `Incluye` (verbatim) + rows of a 5×5 `#D62828` dot and 400/12.5 `#3F4A52` text, 9dp
apart.
26dp: `Políticas de cancelación` — `#F4F5F6` panel radius 16, 15dp padding, title 600/13.5,
body 400/12/1.6 `#5F6A70` (verbatim from `cancellation_policy_details`).
28dp: `Reseñas` + `(N reseñas)`, then `CommentsSection` — 30×30 avatar, name 600/12.5,
relative date 400/10.5 `#5F6A70`, body 400/12/1.6 `#3F4A52`, 16dp apart.
Sticky bottom bar: total 600/17 `#003049` over `por persona · reserva RD$1,500` at 400/10.5,
and a PrimaryButton `Pagar` (verbatim) capped at 158 wide.
Content padding must clear the bar: 96dp.

**13. Galería** **[NEW]** — the existing pager, full screen
Dark in both themes, ground `#0B1B24`. Top bar 14 × 16: a 36×36 `rgba(255,255,255,.14)`
circle with the `cancel` icon 20 white, the counter `3 / 6` 500/12
`rgba(255,255,255,.75)` centered, and a heart in a matching circle right. Centre: the photo
at full width minus 8dp insets, 420 tall, radius 8. Below: a caption 500/13/1.5 white and
`destino · proveedor` 400/11.5 `rgba(255,255,255,.55)`. Bottom, 18dp above a 26dp inset: a
horizontally scrolling strip of 58×58 thumbs radius 7 — current one has a 2dp white border,
others sit at 55% opacity.

**14. Pagar viaje** — `shopping/screen/TripItemPayingScreen.kt`
Back button + `Pagar viaje` (verbatim) 600/17. 20dp: a trip summary card — 1dp `#EDEFF1`,
radius 16, 12dp padding, 78×78 thumb radius 12, title 600/14, meta and provider at 400/11.5.
26dp: `Monto a pagar` (verbatim) 600/15, then two option cards, 11dp apart.
Selected: 1.6dp `#D62828` border, `#FDF4F4` fill, radius 16, 15dp padding; title 600/13.5
`#003049` with a 22×22 radio right — `border-box`, 6dp `#D62828` ring, fully round; amount
600/20 `#D62828`; description 400/11.5/1.5 `#5F6A70`.
Unselected: 1dp `#E2E2E2` border, no fill, title 500/13.5, 1.5dp `#CFD5DA` ring, amount
600/17 `#003049`, no description.
Order: `Pago de reserva solamente` then `Pago total` — note this reverses today's
`ShoppingPaymentWay`, which puts the total first. Descriptions are the existing formatted
strings (`booking_payment_description`, `total_payment_description`).
26dp: `Información de pago` (verbatim) and a payment-method row — 34×24 card mark radius 5
`#003049`, `•••• 4242` 500/13, `Visa · vence 09/28` 400/10.5, chevron. **Open question:
whether this is a real saved card or should open the Stripe sheet.**
22dp: a 1dp `#EEF0F1` top rule, then `Monto a pagar ahora` 500/13 `#5F6A70` against the
amount 600/20 `#003049`. 8dp: the cancellation line at 400/11/1.6.
Sticky bottom: PrimaryButton `Procesar pago` (verbatim).

**15. Procesando pago**
Full-bleed `rgba(255,255,255,.86)` overlay, input blocked. Centered 44×44 ring, 3dp
`#F0DCDC` track with a `#D62828` top edge, 0.8s linear spin. 18dp below:
`Procesando pago…` 500/13 `#003049`.

**16. Listo** — replaces the current post-payment screen
Centered: 104×104 cream tile radius 30 with the logo at 80×80 radius 22, entering with a
pop. 26dp: `Listo` (verbatim) 600/26. 8dp: `Tu lugar en <viaje> está reservado. Te esperamos
el <fecha>.` 26dp: a dashed block — 1dp dashed `#CFD5DA`, radius 16, padding 16 × 22 — with
`Código de confirmación` (verbatim) in caps 400/10.5 and the code 600/20 `#003049` at
0.06em tracking. 20dp: `Monto pagado` (verbatim) 400/12 next to the amount 600/15 `#D62828`.
Bottom: PrimaryButton `Ver mis viajes`, then `Seguir explorando` 500/12.5 `#003049`.

**17. Pago fallido** **[NEW]** — backend exists (`payment/order-failure`,
`CancelPaymentOrderUseCase`)
Centered: 96×96 `#FDF4F4` tile radius 28 with the `cancel` icon at 46 `#D62828`, popping in.
26dp: `No pudimos procesar el pago` 600/24/1.25. 10dp: the reason, default
`Tu banco rechazó la transacción. Intenta otra vez o usa otra tarjeta.` 24dp: a ListGroup
with Viaje / Monto / Método. 16dp: `No se te cobró nada. Guardamos tu lugar por 30 minutos.`
400/11.5. Bottom: PrimaryButton `Intentar de nuevo`, SecondaryButton
`Cambiar método de pago`, muted link `Volver a explorar`.
Send the failure reason to `payment/order-failure` as `PaymentFailureRequest(reason, orderId)`.

### Viajes

**18. Próximos viajes** — `orders/screen/OrderHistoryScreen.kt`
Title `Viajes`. 18dp: two tabs over a 1dp `#EEF0F1` rule — `Próximos viajes` and
`Viajes pasados` (both verbatim), 22dp apart, 11dp bottom padding; active is 600/13.5
`#003049` with a 2dp `#D62828` underline, inactive 400/13.5 `#5F6A70`.
Upcoming card: 1dp `#EDEFF1`, radius 18, clipped. Photo 150 tall with a date badge inset 12
— `rgba(255,255,255,.95)`, radius 10, 600/11 `#003049`. Body 14dp: title 600/15/1.3,
`proveedor · N personas` 400/11.5. 12dp down, above a 1dp `#F2F4F5` rule: `Código` caps
400/10 over the code 600/13 at 0.05em, and the status 600/12 `#D62828` right. Cards 18dp
apart. Tapping one opens the reservation.

**19. Detalles de la reserva** **[NEW screen, existing data]** —
`orders/screen/OrderItemDetailsScreen.kt` currently shows a trip, not an order
Hero 210 tall with a back circle. Body: eyebrow `Detalles de la reserva` (verbatim) in caps
`#D62828`, title 600/21/1.25, meta 400/12.5.
22dp: the confirmation block — 1dp dashed `#CFD5DA`, radius 16, 16dp padding — with
`Código de confirmación` caps over the code 600/19, and the status as a cream pill (padding
6 × 11, radius 999, 600/11 `#D62828`).
**If the balance is unpaid**, 14dp: a `#FDF4F4` panel radius 16, 16dp padding —
`Falta por pagar` 600/13 against the amount 600/19 `#D62828`; then `Pagaste la reserva. El
faltante se puede pagar aquí hasta 24 horas antes de la salida.` 400/11.5/1.6; then a
PrimaryButton `Pagar faltante` at radius 12.
26dp: `Información de pago` ListGroup — Monto pagado, Pago total, Personas.
26dp: `Datos del viaje` (from `trip_info`) as the same two-column grid — Destino, Lugar de
salida, Hora de salida, Hora de llegada.
24dp: the provider row, with `Escríbele al proveedor` (verbatim) 500/11.5 `#D62828` in place
of the rating line.
20dp: the cancellation panel. 26dp: TextLink `Reportar inconveniente con la orden`
(verbatim). 16dp: muted underlined link `Cancelar reserva`.

**20. Pagar faltante** **[NEW — no backend]**
The most important gap. Checkout copy already promises *"paga luego el faltante"* and
`PayItemModel` carries both `initialPayment` and `totalPayment`, but nothing lets a user pay
the remainder. Needs a new endpoint or a reuse of `payment/pending-order` scoped to the
outstanding amount.
Back button + `Pagar faltante` 600/17. 20dp: the trip summary card, with `Orden <código>` as
its third line. 26dp: a `#FDF4F4` hero panel radius 18, 18dp padding — `Monto a pagar` caps
400/10.5, the amount 600/34/1.1 `#D62828`, and `Con esto queda cubierto el viaje completo.`
26dp: `Desglose` ListGroup — `Pago total`, `Reserva pagada` shown as `− RD$1,500`, and a
`Faltante` row on `#FBFBFC` with the label 600/12.5 and the amount 600/15 `#D62828`.
26dp: `Información de pago` row. 14dp: the cancellation line.
Sticky bottom: PrimaryButton `Pagar RD$1,700` — the amount is in the label.
On success the order becomes `Pagado completo` and the balance panel disappears.

**21. Cancelar reserva** **[NEW]** — backend exists (`RemoveOrderUseCase`,
`payment/{id}` DELETE)
BottomSheet, no scrolling body. Title `¿Cancelar esta reserva?` 600/19/1.3. 10dp:
`Faltan más de 24 horas para la salida, así que el reembolso es completo. El proveedor
recibe el aviso al instante.` — this text must reflect the real policy window. 18dp: a
ListGroup with Orden and Reembolso (600/14). 18dp: PrimaryButton `Cancelar reserva`. 12dp:
`Mantener reserva` 600/14 `#003049`, no fill.
Confirming removes the order from Próximos viajes and shows the toast
`Reserva cancelada. El reembolso llega en 5 días hábiles.`

**22. Reportar inconveniente** — `orders/screen/ReportOrderIssueScreen.kt`
Back button + `Reportar inconveniente` 600/16. 22dp: `Cuéntanos tu problema` (verbatim)
600/21/1.3. 8dp: `Orden <código> · <viaje>` 400/12.5. 20dp: a 240-tall textarea — `#FFFFFF`
fill, 1dp `#E2E2E2` border, radius 16, 14dp padding, 400/13/1.6, no resize, placeholder
`Describe lo que pasó con la orden. Mientras más detalle, más rápido lo resolvemos.` 12dp:
`Nuestro equipo responde en menos de 24 horas y el proveedor recibe copia del reporte.`
400/11/1.6.
Sticky bottom: PrimaryButton `Reportar orden` (verbatim), **disabled while the message is
blank** — matching `ReportOrderState.isValid()`. On success, pop back with the toast
`Reporte enviado. Te contactamos pronto.`

**23. Viajes pasados**
TripRow per past trip with a 66×66 thumb: title 600/13.5, `fecha · proveedor` 400/11.
Unrated → a `Calificar` pill on the right (padding 8 × 13, radius 999, `#D62828`, 600/11.5
white). Rated → `ic_star` 12 `#FFB400` + the score 500/11 `#5F6A70`, and no pill. Rows 12dp
apart.

**24. Calificar viaje** — `orders/screen/ReviewOrderScreen.kt`
Back button + `Calificar viaje` 600/17. 20dp: a 64×64 thumb beside the trip title 600/14 and
`fecha · proveedor` 400/11.5. 30dp: `¿Cómo estuvo la excursión?` 600/17/1.35. 16dp: five
stars at 42, 10dp apart — filled `#FFB400`, empty `#E2E2E2`. 12dp: a caption that tracks the
score — `Toca una estrella para calificar`, then Muy malo / Malo / Regular / Bueno /
Excelente. 28dp: `Cuéntanos más` 600/14. 10dp: a 118-tall textarea, `#F4F5F6` fill, no
border, radius 16, placeholder `¿Qué debería saber el próximo viajero?`
Sticky bottom: PrimaryButton `Enviar reseña`. Submitting with no stars shows the toast
`Elige una calificación primero` and does not send. On success, return to Viajes pasados with
`Gracias por tu reseña` and the row flips to rated.

**25. Viajes — vacío**
EmptyState: `No hay viajes que mostrar` (verbatim), `Cuando reserves una excursión aparecerá
aquí.`, action `Explorar excursiones`.

### Cuenta

**26. Favoritos** — `wishlist/screen/WishListScreen.kt`
Title `Favoritos` with `N excursiones guardadas` beneath — singular
`1 excursión guardada`. TripRow with an 88×88 thumb, rating line, price 600/14 `#D62828`, and
a trailing 32×32 `#FDF4F4` circle holding `ic_favorite_filled` 17 `#D62828` that removes the
item. Tapping the row or thumb opens the trip.

**27. Favoritos — vacío**
EmptyState: `Todavía sin favoritos`, `Guarda excursiones con el corazón y vuelve cuando
decidas.`, action `Explorar excursiones`.

**28. Perfil** — `profile/screen/ProfileScreen.kt`
Title `Perfil`. 22dp: a 66×66 cream circle with `ic_profile` 32 `#D62828`, beside the full
name 600/18 and `En Caribeando desde <mes año>` 400/12. 26dp: a ListGroup of label–value
rows — Nombre, Apellido, Correo electrónico, Teléfono (labels verbatim). 14dp: a ListGroup
of navigation rows — Favoritos (`ic_favorite`), Mis viajes (`ic_trip`), Contáctanos
(`ic_address`). 30dp: centered `Cerrar sesión` (verbatim) 600/14 `#D62828`.
**Open question: the data is read-only here, but `UpdatePhoneAndBirthdateRequest` suggests
editing is intended.**

**29. Perfil del proveedor** — `profile/screen/TripProviderProfileScreen.kt`
Cover 150 tall with a back circle. The avatar overlaps it by 30dp: 70×70, radius 20, cream,
3dp white border, logo at 50×50. Beside it, eyebrow `Agencia` (verbatim) caps `#D62828` over
the name 600/18/1.25. 12dp: star 14 + rating 600/12.5 + `(N reseñas)` 400/12.5 `#757575`.
22dp: a stats strip — one ListGroup split into three equal cells by 1dp `#F2F4F5` rules,
15dp × 12dp padding, each a number 600/19 over a caption 400/10.5/1.35 `#5F6A70`:
`Viajes ofrecidos`, `Viajes actuales` (both verbatim), `Meses en Caribeando`.
20dp: the description 400/13/1.65 `#3F4A52`. 26dp: `Viajes actuales` and that provider's
trips as TripRow with 72×72 thumbs.

**30. Contáctanos** **[NEW]** — linked from Perfil and present in `strings.xml`
Back button, 22dp, title `Contáctanos` (verbatim) 600/25. 8dp: `Escríbenos por donde te
quede más cómodo. Respondemos de lunes a domingo, de 7:00 a.m. a 9:00 p.m.` 24dp: a
ListGroup of three channel rows — icon 19, channel 500/13, and a detail line 400/11
`#5F6A70` (`809 555 0100 · respuesta en minutos`, `hola@caribeando.do · menos de 24 horas`,
`809 555 0100`), each with a chevron. **Replace these with real contact details.**
22dp: a `#F4F5F6` panel radius 16 — `¿Es sobre un viaje reservado?` 600/13.5, `Repórtalo
desde la orden y el proveedor recibe copia. Llega más rápido que por aquí.` 400/11.5/1.6,
and `Ir a mis viajes` 600/12.5 `#D62828`.

---

## Interactions & behavior

### Navigation
- Four tab roots — Explorar, Favoritos, Viajes, Perfil — each clearing the back stack, as
  `HomeScreen`'s `NavHost` does today.
- Pushed screens keep a back stack and hide the bottom nav: detail, gallery, checkout,
  balance, order, report, review, provider, calendar, contact.
- Terminal screens clear the stack: success (→ Viajes or Explorar), failure (→ checkout or
  Explorar).
- `Detalles del viaje` is reachable from the feed, favourites and the provider profile.
  `Detalles de la reserva` is reachable only from Próximos viajes.

### Transitions
| Case | Spec |
| --- | --- |
| Push / pop a screen | 300ms, `cubic-bezier(.2,.8,.2,1)`, fade + 14px horizontal slide |
| Bottom sheet in | 320ms, same easing, translate from 100% |
| Scrim | 200ms fade |
| Modal card | 320ms scale 0.6 → 1.06 → 1.0 with fade |
| Success / failure mark | 450–500ms, same pop curve |
| Feed card appear | 350ms fade |
| Toast in | 300ms, same easing, fade + 12px rise |
| Spinner | 800ms linear, infinite |
| Shimmer | 1300ms linear, infinite |

Full-screen overlays (gallery, success, failure, confirm) fade rather than slide.

### Validation
- Signup: passwords must match; the error is inline under the fields, not a modal.
- Report: the button is disabled until the message is non-blank (`ReportOrderState.isValid()`).
- Review: submitting with zero stars shows a toast and blocks; the comment is optional.
- Filters: applying an empty result set is allowed and shows the empty state with a
  `Borrar filtros` exit.

### Loading
Skeletons, never spinners, for first loads — feed, provider, favourites, orders — using the
existing `shimmerEffect`. Spinners only where the user is blocked and waiting on a
transaction: payment processing. Keep the existing `PullToRefreshContainer` on the feed and
Viajes.

### Filtering
Category, budget, date and free text combine (AND). A date chip and a date range are
mutually exclusive — setting one clears the other. `Borrar filtros` resets all four and
closes the sheet.

---

## State management

Extend the existing `ExploreState`, `TripDetailState`, `OrderHistoryState`, `ProfileState`,
`ReportOrderState` and `PaymentState` rather than adding new containers. New state the
redesign needs:

**Explore / filter**
- `selectedCategory: CategoryModel?` — existing
- `maxAmount: Int?` — replaces the slider range with four discrete buckets
- `dateChip: String?` and `dateRange: Pair<LocalDate, LocalDate>?` — mutually exclusive
- `resultCount: Int` — drives the `Ver N resultados` label, needs to reflect pending filters
  before they are applied

**Detail**
- `galleryOpen: Boolean`, `galleryIndex: Int` — the pager index shared with the gallery screen

**Payment**
- `selectedPaymentType` — existing, but the default becomes `BOOKING_PAYMENT`
- `paymentFailure: String?` — the reason string shown on the failure screen and posted to
  `payment/order-failure`

**Order**
- `paidAmount: Double` — **not currently on `UpcomingOrders`.** The model carries
  `totalPayment` only, so the app cannot tell a deposit-paid booking from a fully paid one.
  This field is required for the balance flow.
- `outstanding: Double` — derived, `totalPayment - paidAmount`
- `status: OrderStatus` — the prototype shows `Confirmado`, `Reserva pagada`,
  `Pagado completo`

**Account**
- `isConfirmed: Boolean` — already on `Token`; route to `Cuenta sin confirmar` instead of
  calling `logout()`

### Backend work implied
1. **`paidAmount` on `UpcomingOrders`** — blocks the balance flow entirely.
2. **A pay-balance endpoint** — either a new one or `payment/pending-order` scoped to the
   outstanding amount, then the usual Stripe intent and `payment/order-sucess`.
3. **Cancellation and refund** — `RemoveOrderUseCase` exists but the refund amount and
   window shown in the sheet need to come from the server, not be assumed.
4. **`duración`** on the trip model, if the feed card is to show it (see Open questions).

---

## Assets

Everything is already in the repository — nothing new to produce.

- **Icons** — `composeResources/drawable/*.xml`, used as-is: `ic_search`, `ic_explore`,
  `ic_filter`, `ic_wish_list`, `ic_trip`, `ic_profile`, `ic_favorite`,
  `ic_favorite_filled`, `ic_beach`, `ic_mountain`, `ic_hiking`, `ic_camping`, `ic_pin`,
  `ic_cash`, `ic_clock`, `ic_address`, `ic_arrow_back`, `chevron_right_small`, `cancel`.
  The prototype inlines their exact path data as SVG; in Compose just keep using
  `painterResource`.
- **Star** — `Icons.Filled.Star`, tinted `#FFB400`, as `RatingAndReviews` already does.
- **Logo** — `composeResources/drawable/logo.png` (1024×1024, cream background, red
  flamingo in a map pin). Included here as `brand/logo.png`.
- **Google mark** — `composeResources/drawable/google.png`. Included as `brand/google.png`.
- **No longer used** — `empty_state.png` (1.4 MB) and `book_error_2.xml` are replaced by the
  logo-based EmptyState and the `cancel`-based error state. `caribeandoapp.png` and
  `ic_caribeando_logo.png` are duplicates of `logo.png`.
- **Fonts** — Poppins Light/Regular/Medium/SemiBold/Bold are already bundled. The Eau Sans
  family in `composeResources/font/` is unused by this design.
- **Photography** — none exists. Every image is a placeholder; see Open questions.

---

## Repo file map

| Screen | Existing files |
| --- | --- |
| Explorar | `explore/screen/ExploreScreen.kt`, `explore/composable/TripItem.kt`, `CategoryItem.kt`, `CategorySection.kt`, `common/SearchBar.kt` |
| Filtrar | `explore/composable/HomeFilterContent.kt`, `common/AmoutSliderPicker.kt`, `common/ChipFilter.kt` |
| Calendario | `common/ModernDateRangePicker.kt`, `common/ModernDatePicker.kt` |
| Detalles del viaje | `shopping/screen/ShoppingItemDetailScreen.kt`, `shopping/composable/*`, `common/CommentsSection.kt`, `CancellationPolicy.kt`, `MapDetails.kt`, `RatingAndReviews.kt`, `ReadMoreText.kt` |
| Galería | `shopping/composable/ShoppingitemHeader.kt`, `common/TripItemBar.kt` |
| Pagar viaje | `shopping/screen/TripItemPayingScreen.kt`, `shopping/composable/ShoppingPaymentWay.kt`, `common/CustomRadioButton.kt`, `stripe/PaymentViewModel.kt` |
| Pago fallido | `data/network/services/PaymentService.kt`, `data/model/PaymentFailureRequest.kt`, `domain/usecase/CancelPaymentOrderUseCase.kt` |
| Viajes | `orders/screen/OrderHistoryScreen.kt`, `orders/composable/UpcomingOrderTripItem.kt`, `PreviousTripItem.kt`, `common/EmptyState.kt` |
| Detalles de la reserva | `orders/screen/OrderItemDetailsScreen.kt`, `orders/viewmodels/WishListOrderDetailViewModel.kt` |
| Pagar faltante | no existing screen; `domain/models/PayItemModel.kt`, `PaymentService.kt` |
| Cancelar reserva | `domain/usecase/RemoveOrderUseCase.kt`, `PaymentService.removeOrder` |
| Reportar inconveniente | `orders/screen/ReportOrderIssueScreen.kt`, `shopping/state/ReportOrderState.kt`, `shopping/viewmodels/ReportOrderViewModel.kt` |
| Calificar viaje | `orders/screen/ReviewOrderScreen.kt`, `orders/viewmodels/RatingOrderViewModel.kt`, `common/PendingReview.kt` |
| Favoritos | `wishlist/screen/WishListScreen.kt`, `wishlist/composable/WishItem.kt`, `common/FavoriteButton.kt` |
| Perfil | `profile/screen/ProfileScreen.kt`, `profile/composable/ProfileHeader.kt`, `ProfileItem.kt`, `ProfileDetailItemSection.kt` |
| Perfil del proveedor | `profile/screen/TripProviderProfileScreen.kt`, `common/ProviderProfileOverview.kt`, `ProviderStats.kt`, `ProviderStatsItem.kt` |
| Login / signup / recuperar | `signup/screen/LoginScreen.kt`, `SignUpAccountScreen.kt`, `FillSignUpAccountScreen.kt`, `domain/usecase/RequestResetPasswordEmailUseCase.kt` |
| Cuenta sin confirmar | `data/model/Token.kt`, `data/imp/LoginRepositoryImpl.kt`, `home/screen/HomeScreen.kt` |
| Contáctanos | no existing screen; `values/strings.xml` (`contact_us`) |
| Error states | `common/ErrorModal.kt`, `common/ErrorState.kt` |
| Bottom nav | `common/BottomNavigationBar.kt`, `home/model/BottomNavigationItem.kt` |
| Theme | `presenter/theme/Colors.kt`, `Font.kt` |

### New screens
`¿Olvidó su contraseña?`, `Cuenta sin confirmar`, `Calendario` (extracted from an inline
component), `Galería`, `Pago fallido`, `Detalles de la reserva` (repurposed),
`Pagar faltante`, `Cancelar reserva`, `Contáctanos`.

---

## Open questions

Resolve these with the design owner before building the affected screens.

1. **Photography.** Every image is a placeholder. The design is image-forward — a 216-tall
   feed photo and a 300-tall detail hero — and will not hold up without real photos.
2. **`duración` on the feed card.** The card meta line reads `destino · fecha · duración`,
   but `TripModel` has no duration field. Either add it or drop it from the card.
3. **Saved card at checkout.** Shown as `•••• 4242`. If saved cards do not exist, that row
   should open the Stripe payment sheet instead.
4. **Editable Perfil.** Read-only in this design, but
   `UpdatePhoneAndBirthdateRequest` exists. If editing ships, it needs an edit screen that
   is not designed here.
5. **Pagar faltante backend.** No endpoint exists. Without one this screen cannot ship,
   and the checkout copy promising *"paga luego el faltante"* stays unfulfilled.
6. **Cancellation policy.** The sheet states a full refund inside 24 hours, taken from
   `cancellation_policy_details`. Confirm the real policy and whether the refund amount
   comes from the server.
7. **Language.** Spanish only, matching `strings.xml`. Add an English pass if the app is
   meant for tourists as well as residents.

## Files in this bundle

| File | What it is |
| --- | --- |
| `Caribeando App.dc.html` | The clickable prototype — all 31 screens, light and dark |
| `Caribeando Screens.dc.html` | Every screen laid out on one board, grouped by flow |
| `Caribeando Redesign.dc.html` | The four original directions; **1a** was chosen |
| `support.js` | Runtime the three HTML files need — keep it alongside them |
| `brand/logo.png` | Copied from `composeResources/drawable/logo.png` |
| `brand/google.png` | Copied from `composeResources/drawable/google.png` |
| `github.md` | Repo association, sync record and screen map |

The prototype has a tweaks panel exposing `theme` (light/dark), `startScreen` (jump to any
of the 31 states), `depositFirst` (feed cards leading with the deposit instead of the total —
an unchosen alternative, kept for discussion), `failPayment` (force the failure path) and
`latency` (loading-state duration).
