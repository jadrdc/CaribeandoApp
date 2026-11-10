---
name: verify
description: |
  Run the verification gate for the Caribeando KMP client after implementation — build the
  affected target(s), run available tests, and report a pass/fail summary. Fire when the user
  says "verify", "run the checks", "does it build", or at the end of a build step.
user_invocable: true
---

# /verify

Prove the current change is shippable. Caribeando has no lint/detekt, no CI, and an unconfirmed
test setup as of 2026-08-18 (see the gaps in `.agents/rules/ci-build-config.md`) — this skill is
intentionally thinner than a mature project's `/verify` until those exist. Don't claim a check
passed that wasn't actually run.

### 1. Build

```bash
./gradlew :composeApp:assembleDebug          # Android
./gradlew :composeApp:compileKotlinIosArm64  # iOS, compiles the shared code without a full Xcode build
```

Confirm real dependency versions in `gradle/libs.versions.toml` before assuming a Gradle task
name is current — Gradle/AGP task names shift across major-version bumps (relevant right now
given the pending upgrade to a newer Compose Multiplatform/AGP).

### 2. Tests

`composeApp/src/commonTest/.../ComposeAppCommonTest.kt` exists; run:

```bash
./gradlew :composeApp:allTests
```

If this is the only test file, say so plainly in the report rather than implying broad coverage.

### 3. Manual/visual check (until Maestro or similar exists)

For UI changes, there is no automated E2E yet. State explicitly in the report that the change
needs a manual pass on-device/emulator before merging — don't imply automated UI coverage that
doesn't exist.

### 4. Report

```
Verify — <change>
Build:   PASS/FAIL (assembleDebug | compileKotlinIosArm64)
Tests:   PASS/FAIL (commonTest — N test(s) run) | NONE FOUND
Manual:  NOT RUN — needs a device/emulator pass (no E2E automation configured yet)
```

## Stop conditions

Build failure surviving one fix attempt · a Gradle/version-catalog change needed that wasn't
part of the plan · anything that would require guessing at an unread file's contents to fix.

## Anti-patterns

- Don't report a check as passing without having actually run it.
- Don't silently add ktlint/detekt/Maestro config as a side effect of an unrelated feature —
  propose it explicitly first (real gap, but a deliberate one to fill, not a drive-by).
