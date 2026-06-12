# FORGE — The Discipline Operating System

**The discipline app that can't be lied to.** Your phone's own data — screen
time, GPS, the camera, your sleep window — is the lie detector for your
commitments. Three momentum scores, verified. *You don't manage FORGE. You
report to it.*

Full product spec: [`docs/MASTER_PLAN.md`](docs/MASTER_PLAN.md).

## Layout

```
forge/
├── core/   Pure-Kotlin engine. No Android dependency. Fully unit-tested.
└── app/    Android shell (Kotlin + Compose + Room). Needs the Android SDK.
```

`:core` is the product: all scoring runs here, on-device, deterministic,
testable on any JDK. `:app` is included by Gradle only when `ANDROID_HOME`
is set, so engine work and CI never need an Android SDK.

### `:core` map

| Package | What lives there |
|---|---|
| `scoring` | 7-day weighted **momentum** model + Mind / Productivity / Health day scorers |
| `signals` | Sleep-window derivation from unlock timestamps; **Impulse Detection** (late-night sessions, switch loops, binges) |
| `interventions` | Checkpoint (cost + 10s wait), **Iron mode** (24h-cooldown early exit), **Monolith** focus contracts |
| `contracts` | Daily contracts: AI proposes → user confirms → engine verifies |
| `ledger` | The Ledger entry types — *events, never content* is enforced by the type system |
| `retention` | The Shadow, Comeback Protocol (72h / 3 wins), Weekly Tribunal assembler (incl. excuse↔late-night correlation) |
| `persona` | **DistressGuard** — the non-negotiable crisis override, persona off, real human support |
| `intent` | The input engine's structured action schema (one stateless LLM function-calling pass) |

## Build & test (engine)

```bash
cd forge
gradle :core:test
```

49 tests encode the spec's promises: one bad day dents but never zeroes;
recovery shows within 48h; growth input caps at +10; Iron early exit waits
out a full 24h cooldown; a voided Monolith hurts more than never declaring
one; the Tribunal correlates "tired" with phone-past-1am nights.

## Build (Android app)

Requires Android SDK (API 35) and network access to `google()` maven:

```bash
ANDROID_HOME=~/Android/Sdk gradle :app:assembleDebug
```

## Hard architecture rules (do not bend)

1. Local-first — all scoring on-device.
2. Events, never content — no URLs, no sites, no screen text, anywhere.
3. Any future sync is E2E or local-network; rows are already shaped for it.
4. Distress override is wired before any persona string ships.

## Status vs build order (§12 of the plan)

- [x] Week 2–6 engine: scoring math, momentum model, categorization, signals — **done, tested**
- [x] Intervention state machines (logic): Checkpoint / Iron / Monolith — **done, tested**
- [x] Retention logic: Shadow / Comeback / Tribunal — **done, tested**
- [ ] Week 1–2 de-risk spike: CV rep counting on-device (MediaPipe) — needs a physical device
- [ ] Overlay + accessibility service UI wiring (skeletons in place)
- [ ] Intent router LLM pass (schema in `core/intent`; Anthropic call in app layer)
- [ ] Dashboard polish, rituals, share cards
