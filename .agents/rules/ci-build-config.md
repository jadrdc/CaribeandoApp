# CI & Build Configuration

Status as of 2026-08-18 — read this before assuming a gate exists that doesn't.

## What's confirmed present

- Version-catalog-based Gradle setup (`libs.versions.toml`, referenced via `alias(libs.plugins.*)`
  in the root `build.gradle.kts` — the file itself was unreadable at scaffolding time).
- `google-services` + Firebase Crashlytics Gradle plugins applied.
- A Google Maps secrets Gradle plugin (`libs.maps.secrets.gradle.plugin`) — implies a
  `secrets.properties` / `local.defaults.properties` pattern for the Maps API key, consistent
  with the `secrets.properties` and `local.defaults.properties` files present at the project
  root.

## Real gaps, not yet filled

- **No CI** (no `.github/workflows/`, no Bitrise config, nothing found).
- **No git repository yet** — `git init` hasn't happened. Nothing branch/PR-related applies
  until it does.
- **No lint** (ktlint/detekt or equivalent) configured.
- **No E2E automation** (no Maestro or similar) — UI changes need a manual device/emulator pass.
- **No documented flavor/environment split** (dev/staging/prod) was found — `Constants.kt`
  (which likely holds the base URL) was unreadable at scaffolding time; confirm whether there's
  one hardcoded URL or a real flavor system before assuming either.

## If/when a CI pipeline gets built

GitHub Actions is the natural choice once this repo has a real git remote (rather than
Bitrise, which Cineco uses but which isn't already in play here) — a matrix job for
`assembleDebug` (Android) + `compileKotlinIosArm64` (iOS, doesn't need a macOS runner or Xcode
just to type-check the shared code) is a reasonable first pass, with a macOS runner added later
for a real iOS build/TestFlight upload once that's worth the cost.

## Version-catalog coupling — read before touching `gradle/libs.versions.toml`

**Confirmed working as of 2026-08-18: `./gradlew :composeApp:installDebug` builds and installs
on Android** with the versions below. This was a multi-round debugging session (three build
failures in a row, each looking like a different root cause) — the actual lesson is at the
bottom of this section, read that even if you don't care about the specific versions.

Current pins and why (full narrative reasoning also lives in the `libs.versions.toml` header
comment itself — that's the primary source; this is the condensed, skimmable version):

| Dependency | Pinned to | Why NOT the newer version |
|---|---|---|
| AGP | `8.13.0` | AGP 9.0+ breaks a single module applying `com.android.application`/`library` together with the KMP plugin — exactly this project's `:composeApp` shape. Fixing that is a module-split project, not a version bump. |
| Kotlin | `2.3.20` | Needed once (see below) — not itself the fix for most of this session's errors. |
| Coil3 | `3.4.0` | Coil's own Kotlin-metadata version must be ≤ what the project's Kotlin compiler can read. 3.5.0 needs Kotlin 2.4.0 (unreadable by us) and drops `iosX64`; 3.3.0 needs Kotlin 2.2.0 (now behind us). **Coil and Kotlin version must move together** — check Coil's changelog for its stated Kotlin version before bumping either. |
| KSP | `2.3.9` | KSP's versioning scheme changed at its own `2.3.0` release — no longer a `<kotlinVersion>-<kspVersion>` pair, now targets a Kotlin *language* version independently. Don't look for a `2.3.20-x.x.x`-shaped string; it doesn't exist post-2.3.0. |
| kotlinx-datetime | `0.6.2` | **Do not bump past this without a plan.** 0.7.0 removed `kotlinx.datetime.Instant`/`Clock` in favor of `kotlin.time.Instant`/`Clock`; 0.8.0 ships without them at all (separate compat artifact, not in this catalog). This project's code imports `kotlinx.datetime.Clock`/`Instant` throughout (12 files) and relies on `kotlinx.datetime.Instant`'s built-in ISO-8601 `kotlinx.serialization` default on `@Serializable` fields like `TripDetails.leaving_time` — `kotlin.time.Instant` has no equivalent built-in serializer yet ([kotlinx.serialization#3026](https://github.com/Kotlin/kotlinx.serialization/issues/3026)). Migrating to `kotlin.time.Instant` is a real, deliberate change (rewrite imports in all 12 files + hand-write a serializer + confirm the wire format still matches `caribeando-backend`) — not something to do as a side effect of a routine "upgrade everything" pass. |

**The methodology lesson, which matters more than any specific version number:** a plausible-
sounding root-cause diagnosis (even one citing a real changelog) can still be wrong. The
"Kotlin 2.2.20 can't compile the new time API" theory looked right and cited real sources, but
bumping Kotlin to 2.3.20 left the exact same errors unchanged — which was the signal it was the
wrong diagnosis. The actual fix only came from grepping the real source imports
(`grep -rl "import kotlinx.datetime.Clock"`) and checking what `kotlinx-datetime`'s own
changelog said about *that* version, not from pattern-matching the error text to a familiar-
sounding explanation. **Before applying a version bump to fix a build error: grep for the
actual failing symbol's import across the codebase first, and if a fix doesn't change the error
output at all, that's a signal to re-diagnose from the source rather than try a bigger version
of the same fix.**
