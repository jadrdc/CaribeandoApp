---
name: wrapup
description: |
  End-of-session state capture for the Caribeando KMP client: summarize what was built, what's
  unverified, and what's next. Fire when the user says "wrap up", "we're done for now", or
  after a feature's verify passes.
user_invocable: true
---

# /wrapup

Capture the session's end state so the next session (or Agustin) can resume cold, without
re-deriving context that's already known.

### 1. Summarize what shipped

- Files created / modified.
- What was actually verified (build/tests) vs. what still needs a manual pass.
- Anything inferred rather than confirmed (e.g. a DTO field guessed from usage, not read
  directly) — call these out explicitly so a future session doesn't treat them as settled.

### 2. Update project memory if this is worth remembering across sessions

If the change is a real architectural decision, a newly-confirmed fact (e.g. "DI is
confirmed Koin, not inferred"), or a new gap discovered, consider whether it belongs in this
session's project memory (`caribeando_overview.md` or a new topic file) rather than only in
this repo's docs — memory persists across sessions even when this repo isn't open.

### 3. Next-action summary

```
<feature/change> — <one-line title>
State:   <built | verified | needs manual QA>
Files:   <count> created / <count> modified
Next:    <what's left, if anything>
```

## Anti-patterns

- Don't mark something "done" that only built but was never actually run/tested.
- Don't bury an inferred-not-confirmed fact inside prose where it'll get treated as settled later.
