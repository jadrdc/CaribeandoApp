---
name: commit-hygiene
description: |
  Branch naming, commit message format, and never-stage rules for the Caribeando KMP client.
  Consulted by implement-feature and any flow that commits. Fire when the user says "commit
  this", "open a PR", "what's the branch convention", or before any git commit.
user_invocable: true
---

# commit-hygiene

**Prerequisite:** this repo has no `.git` yet as of 2026-08-18. Nothing below applies until
`git init` (or a clone from a real remote) has happened and there's a first baseline commit.
Flag that explicitly rather than silently skipping it if asked to commit before it exists.

## Branches

Once under version control, cut feature branches from `main` (adjust here the first time a
`develop`/integration branch convention is actually adopted):

| Kind | Pattern | Example |
|---|---|---|
| Feature | `feature/{short-slug}` | `feature/whatsapp-contact-button` |
| Bugfix | `bugfix/{short-slug}` | `bugfix/favorite-toggle-race` |
| Chore | `chore/{short-slug}` | `chore/bump-compose-multiplatform` |

Lowercase, kebab-case slug. No ticket-ID prefix — Caribeando doesn't use Jira; if a GitHub Issue
number exists, `feature/{issue-number}-{slug}` is fine.

## Commit messages

```
Brief description of the change

Optional body explaining what changed and why (not how).
```

- Present tense ("Add WhatsApp button", not "Added WhatsApp button").
- First line under 72 characters.
- Body explains *why* when it's not obvious from the diff alone.

## AI attribution

Do not add `Co-Authored-By` or tool-name attribution unless the user has explicitly asked for
it in this session — match whatever convention the human collaborators on this repo actually
use once there are any.

## Never-stage files

Never `git add`: `local.properties`, `secrets.properties`, `local.defaults.properties`,
`google-services.json`, `GoogleService-Info.plist`, any `*.jks`/`*.keystore`, `.env`/`*.env`,
`.gradle/`, `build/`, `.kotlin/`. Stage files by name; avoid `git add -A` / `git add .` in this
repo specifically, since several of the above already sit at the project root alongside
source (confirmed present: `secrets.properties`, `local.properties`,
`composeApp/google-services.json`).

## PR hygiene (once a remote/PR workflow exists)

- One PR = one logical change.
- Never force-push a shared branch.
- Don't merge your own PR without at least a self-review pass against `.agents/rules/`.
