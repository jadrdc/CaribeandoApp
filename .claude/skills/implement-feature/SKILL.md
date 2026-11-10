---
name: implement-feature
description: |
  Implement a single Caribeando client feature end-to-end: read the contract, build (domain →
  data → presenter), verify, wrap up, commit. Fire when the user says "implement X", "build
  feature X", or any request to take one feature from description to done on the KMP client.
user_invocable: true
---

# /implement-feature <description>

Single-feature implementation for the Caribeando KMP client. Lighter than a Jira-driven,
multi-module, worktree-based flow (that's Cineco's setup, not this one) — Caribeando is a
single `:composeApp` module with no ticket system, so this is a straight linear flow.

## Steps

### 1. Load context

Read `CLAUDE.md`, `.agents/rules/architecture-layers.md`, `.agents/rules/patterns-conventions.md`,
and — for anything networked — `.agents/rules/api-contract.md` **and the actual backend
controller/DTO source it points to**, not an assumption from a prior session's notes.

### 2. Confirm scope before writing code

State in one or two sentences what will change and where (which `domain`/`data`/`presenter`
files), especially if the request is ambiguous or touches money, auth, or an existing screen a
lot of other code depends on. This project doesn't have a spec/plan document step — this
confirmation is the substitute gate.

### 3. Build — domain + data first

Domain models, use case(s) (`{Action}{Entity}UseCase`, returning `OperationResult<T>`),
repository interface changes if needed, then the `data` layer (DTO, service method, repository
implementation, mapper). Never invent a field or endpoint — resolve against
`.agents/rules/api-contract.md`.

### 4. Build — presenter

ViewModel (extends `GenericViewModel`, exposes `OperationResult`-driven state), screen/composable
work following the existing Screen pattern in that feature's package, reusing
`presenter/common/` components where one already exists (check before writing a new one —
e.g. `TrustBadge.kt` and `WhatsAppButton.kt` already exist there as of 2026-08-18).

### 5. Verify

Run [`/verify`](../verify/SKILL.md). Fix on the first failure; if it survives one fix attempt,
stop and report with full context rather than guessing further.

### 6. Wrap up + commit

Run [`/wrapup`](../wrapup/SKILL.md), then commit per
[`commit-hygiene`](../commit-hygiene/SKILL.md) — checking first whether this repo has been
initialized with git yet (as of 2026-08-18 it hasn't).

## Stop conditions

Build/test failure surviving one fix attempt · a needed backend field/endpoint that doesn't
exist · an architectural choice not covered by `.agents/rules/` · uncertainty about whether a
file you need to edit is actually current (re-check for iCloud placeholder issues before
trusting a read that came back suspiciously empty).

## Anti-patterns

- Don't skip reading the actual backend source for a networked feature.
- Don't invent DTO fields to make a screen "just work."
- Don't silently expand scope beyond what was confirmed in step 2.
- Don't claim verify passed without having run it.
