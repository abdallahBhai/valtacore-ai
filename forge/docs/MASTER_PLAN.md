# FORGE — Master Plan
**The Discipline Operating System**
*Consolidated product spec · Final feature set · Roadmap · Build order*

---

## 1. The One-Line Thesis

**FORGE is the discipline app that can't be lied to.** It uses your phone's own data — screen time, GPS, the camera, your sleep window — as a lie detector for your commitments, scores your self-control with momentum instead of streaks, and confronts you weekly with the evidence.

Tagline candidates: *"You don't manage FORGE. You report to it."* / *"Your phone testifies."*

## 2. Positioning

- **Audience:** ambitious men, 16–30, global English-first. The self-improvement / monk-mode / deep-work wave. Brand is men-focused; the engine is neutral (no gendering in code or data models) so expansion later is a rebrand, not a rebuild.
- **Category:** verified discipline — not a habit tracker, not a fitness app, not a planner. Competitors track behaviors; FORGE verifies execution.
- **Voice:** drill-sergeant mentor. Terse, honest, names wins as firmly as failures. Never cheerleads, never shames into despair. Hard rule: distress override — if user inputs signal genuine crisis, the persona drops instantly and the app points to real human support. Non-negotiable, built day one.
- **Privacy as a weapon:** all scoring on-device. **Events, never content** — log "checkpoint triggered, category restricted, deflected, 23:41," never URLs, never sites. "We couldn't leak your data if we wanted to" is a headline feature.
- **Distribution:** word-of-mouth via share cards. No ad budget assumed.

## 3. The Core System — Three Verified Scores

No unified score. No leaderboards. Each score is a rolling 7-day weighted **momentum** model (one bad day dents, doesn't zero; recovery visible within ~48h; trajectory arrow shown, not just the number).

### Mind
- **Growth input:** time in audio/reading apps (Audible, podcasts, Kindle) counts positively — *capped* (max ~+10 of score) so passive consumption can't be farmed. We score content, we never host it.
- **Sleep window adherence:** derived free from usage data — last unlock at night, first unlock in morning. No wearable needed.

### Productivity
- **Distraction control:** app-usage scoring (drain apps lower it) + **Impulse Detection** — pattern recognition (late-night solo sessions, rapid app-switching loops, binge bursts), not content reading.
- **Monolith sessions:** hardware-enforced focus contracts. Declare a duration, distraction apps lock, leaving early voids the session and logs a *broken promise*. Verified deep work.
- **Daily contracts:** AI-planned commitments (see §5), confirmed each morning, verified by the engine.
- Tasks remain as a light self-reported layer, weighted low.

### Health
- **Steps** (pedometer / HealthKit / Health Connect).
- **GPS gym check-ins:** verified attendance.
- **CV-verified workouts:** front camera + on-device pose estimation (MediaPipe/ML Kit) counts reps. V1 scope: 3–4 clean exercises — squat, push-up, plank, lunge. Camera feed never leaves the device. Framing: Health scores *consistency*, not performance.

## 4. Intervention Layer

- **Checkpoint mode (default):** overlay on restricted sites/apps — "Proceeding costs you. Mind –4." Optional 10-second wait before Proceed activates. Every event logged as deflected/proceeded; deflections are visible progress.
- **Iron mode (strict):** hard block, entered as a commitment with a duration. Early exit costs score, is logged, and takes effect only after a **24-hour cooldown** — the urge passes before the unlock does.
- **Detection:** local domain-list matching (maintained open-source blocklists) via accessibility service; whole-app shielding on iOS later. Android can attempt surgical reels-blocking; iOS gets whole-app limits.

## 5. The Input Engine & Coach

- **One entry point for everything:** text + voice. "Logged 50 pushups." "Move gym to 8." "Skipping today, exhausted." "New goal: run a 10k by September."
- **Architecture:** stateless intent router — ~10–15 structured actions (log / mutate / declare / query) via one LLM function-calling pass. No open chat, no conversation memory in V1. Parse → act → terse confirmation ("Recorded. Health +2.").
- **Excuses become data:** stated excuses are timestamped and stored; the Tribunal correlates them ("'Tired' ×4 this month. Three of those nights: phone past 1am.").
- **Negotiation friction:** deferrals trigger one confirmation ("Second deferral this week. Confirm?"). Overrides allowed, always logged.
- **Rituals:** morning brief (today's contract, accept) + evening debrief (30-second voice report). Two touchpoints a day keep the system current.
- **Coach persona:** lives in the output voice across all system messages, not in conversation depth. Can answer bounded questions about *your own data*. Premium later: weekly "sit-down" reviews. Never a companion bot.

### AI Planning
- **Goal decomposition (once per goal):** vague goal → milestone roadmap with weekly targets and required habits. One LLM call.
- **Daily contract (V1):** AI proposes today's commitments from the roadmap; confirming converts them to promises the engine verifies. *Plan → enforce → verify → score* — the closed loop no planner has.
- **Weekly adaptive planning (V1.5):** Sunday replan from actual performance. No month-long static timeblocks. No calendar sync in V1.

## 6. Retention & Identity Mechanics

- **The Shadow (V1):** compete against yourself 30 days ago — every score, every day. Competition with zero network effects and zero cheating incentive.
- **Comeback Protocol (V1):** structured 72-hour relapse recovery — scores frozen, three small mandatory wins, then normal scoring resumes. Built for the exact moment habit apps lose users: the day after failure.
- **Weekly Tribunal (V1):** the evidence report. Firm on failures, names the wins, surfaces excuse patterns. The coach's signature moment.
- **The Ledger (V1, simple view):** scrollable record of every promise, excuse, checkpoint event, and verdict. FORGE's "memory" — evidence, not notes. (Obsidian-style knowledge base: permanently rejected; different job, different app.)
- **Blueprints (V2):** downloadable discipline *protocols* — "The Operator": sleep 10pm–5am, Iron till noon, 4 Monolith hours. Configuration, not commerce. Creator-published configs with rev share in V2/V3.

## 7. Growth Engine — Share Cards (V1 launch requirement)

Strava-anatomy trophies: **earned** (only exist if verified), **legible** (one hero stat), **identity-signaling** (posting says "I'm disciplined," not "I use an app").

- V1 cards: **Verified workout** ("47 push-ups · camera-verified"), **Monolith** ("3h 12m deep work · zero breaches"), **Iron streak** ("Day 30 · Iron Mode held").
- Design rules: dark, typographic, luxury-receipt aesthetic; small "✓ FORGE-verified" seal (the curiosity gap that drives installs); 9:16 story-first; share prompt appears at the dopamine peak, two taps, never auto-post, never beg.
- V1.5: Comeback card, **Proof of Discipline** career card ("184-day record · 91% promises kept").
- V2: **Annual Tribunal** — Spotify-Wrapped-style yearly evidence reel, ships in December.

Loop: verified moment → trophy → story → seal curiosity → install → their first verified moment. CAC ≈ 0.

## 8. Business Model

**Freemium subscription. No ads. No affiliate. No marketplace.**

- Pricing: **$9.99/mo · $59.99/yr** (annual pushed hard).
- **Free:** core verification engine, scores, checkpoint mode, basic share cards, limited voice logs/day.
- **Premium:** AI planning (goal decomposition + daily contracts), unlimited voice logging, Iron mode customization, excuse-pattern analytics, extended Ledger history, weekly coach reviews (later), Seasons & Stakes (V2).
- **V2 revenue layers (on-theme):** **Stakes** — money collateral on commitments, forfeits on breaks (loss aversion, StickK-proven); **Seasons** — paid 30/60/90-day named intensives ("Winter Forge," "Monk Mode 60") with permanent record badges.
- **Removed:** affiliate marketplace & celebrity wardrobes — values clash (consumption vs. discipline), likeness-rights exposure, and per the revenue model it was ~$0.51/active user — a rounding error. Monetization now profits when users go *harder*, never when they *buy*.
- Revenue model lives in `FORGE_Revenue_Model.xlsx` (affiliate line to be zeroed). Base case: ~$15k net Y1 → ~$760k Y5 at 4% conversion. Year 1 is small in every scenario — the Y1 job is retention proof, not revenue.

## 9. The Cut List (decided, with reasons — do not relitigate)

| Cut | Why | Replaced by |
|---|---|---|
| Unified Integrity Score | "What does 73 mean" problem | Three separate verified scores |
| Global/national leaderboards | Cheating incentive + cold start | Shadow (V1), Pacts (V1.5), territory wars (V3, density-gated) |
| Book-summary / podcast library | Content business inside a measurement product; rewards passive consumption | Score consumption time, capped |
| Celebrity wardrobe affiliate | Likeness-rights exposure + theme contradiction | Blueprints as protocols; archetypes only if ever needed |
| Obsidian-style memory vault | Anti-thesis (organizing ≠ executing); unwinnable matchup | The Ledger + goal decomposition |
| Month-long AI timeblocking | Static plans age out in days; score punishes stale plans | Daily contracts + weekly adaptive replan |
| URL/content reading via VPN | Privacy radioactive; iOS near-impossible | Pattern detection + local domain-list matching; events-not-content |
| Marketplace (all commerce) | Perverse incentive; betrays the creed | Stakes + Seasons |
| Desktop app in V1 | Second codebase + sync breaks local-first | Browser extension V1.5; native clients V2 with E2E sync |
| Open-chat companion bot | Cost scales with loneliness; dependency risk for this audience | Scripted persona + bounded data queries + distress override |

## 10. Roadmap

### V1 — The Wedge (Android, local-first)
Screen-time engine · three momentum scores · Checkpoint + Iron mode · Monolith · CV workouts (4 exercises) · steps + GPS check-ins · sleep window · voice/text input engine with intent router · coach voice pass · goal decomposition + daily contracts · Shadow · Comeback Protocol · Weekly Tribunal · Ledger view · share cards (workout / Monolith / streak).
**Promise: your phone can't lie to you anymore.**

### V1.5 — The Loop Closes
Browser extension (Chrome/Edge/Firefox — site tracking + Monolith blocking on desktop, ~80% of desktop signal for ~5% of cost) · weekly adaptive AI planning · Brotherhood Pacts (2–5 friends, promise-kept/broken visibility only, viral loop) · Proof of Discipline card · solo territory capture (your map fills in as you run; the Shadow defends last month's ground).

### V2 — The OS Emerges
Native desktop clients with **E2E-encrypted sync** (architecture decision made now, built later) · iOS port (Screen Time entitlement gauntlet, with traction in hand) · wearable integrations via Health Connect · Stakes · Seasons · Annual Tribunal (December) · Blueprints with creator configs · weekly coach sit-downs (premium).

### V3 — The Platform
Multiplayer weekend territory wars, **density-gated** (switch on per city at ~500+ actives: "the war begins in Dhaka this weekend") · creator blueprint marketplace (rev share, consent-based) · FORGE-verified as social proof.

## 11. Tech Stack & Architecture Rules

- **Platform:** Android first (UsageStats vs. Apple's entitlement process; surgical reels-blocking possible; ship-and-iterate speed). iOS in V2.
- **Stack suggestion:** Kotlin + Jetpack Compose; Room/SQLite local store; MediaPipe pose; on-device speech-to-text; Anthropic API for the intent router / decomposition / Tribunal generation (stateless, short outputs — pennies per user; heavy AI features premium-gated so heaviest users are paying users).
- **Permissions gauntlet:** UsageStatsAccess + accessibility service + SYSTEM_ALERT_WINDOW. Play Store requires a digital-wellbeing core-purpose declaration — budget one rejection-and-resubmit cycle.
- **Hard architecture rules:** (1) local-first, all scoring on-device; (2) events-not-content in every log line; (3) any future sync is E2E or local-network — decided now in the data model; (4) distress override floor in the persona layer from day one.

## 12. Build Order (solo + Claude Code)

1. **Week 1–2 — De-risk spike:** CV rep-counting prototype (riskiest tech) + UsageStats data pull. If pose detection disappoints, Health falls back to GPS+steps and CV moves to V1.5 — decide early, not at month four.
2. **Week 2–6 — The engine:** usage collection, app categorization, scoring math (momentum model), three-score dashboard. Everything feeds on this.
3. **Week 5–9 — Interventions:** Checkpoint overlay, Iron mode + cooldown, Monolith enforcement.
4. **Week 8–12 — Input engine:** intent router (text first), then voice; Ledger view.
5. **Week 11–14 — AI layer:** goal decomposition, daily contracts, morning/evening rituals.
6. **Week 13–16 — Health:** steps, GPS check-ins, CV integration from the spike.
7. **Week 15–19 — Retention & growth:** Shadow, Comeback Protocol, Tribunal, share cards.
8. **Week 18–20 — Voice pass:** coach persona across every string; distress override tested.
9. **Week 20–24 — Beta:** 20–50 users from r/getdisciplined / fitness Discords; Play Store submission with rejection buffer.

**Honest timeline: ~5–6 months to public beta** alongside university + the Fiverr/agency track — it assumes FORGE gets consistent weekly blocks, not leftovers. Worth consciously slotting against the existing course-and-freelance calendar rather than letting them silently fight.

## 13. Validation Gates (decide with numbers, not feelings)

- **Beta gate:** D7 retention ≥ 25%, D30 ≥ 12–15% (habit-app D30 is typically ~5–10%; beating it is the whole thesis). Below that: fix retention, add nothing.
- **Share gate:** ≥ 10% of weekly actives generate a card; track installs-per-card.
- **Monetization gate (post-launch):** free→paid ≥ 2% by month 3 of monetizing; 4% validates the Base case.
- **Kill/pivot criterion:** if D30 < 8% after two retention iterations, the verified-discipline wedge isn't landing — re-examine before V1.5.

## 14. Top Risks

1. **Play Store approval** for accessibility/overlay permissions — mitigated by careful policy declaration; budget resubmits.
2. **CV accuracy** across phones/lighting — mitigated by the week-1 spike and narrow exercise set.
3. **Retention reality** — the audience that needs discipline apps churns hardest; Shadow + Comeback + contracts are the bet.
4. **Founder bandwidth** — university + freelancing + FORGE is three jobs; the build order front-loads the riskiest items so a stall fails fast and cheap.
5. **Copycats** — overlays are copyable in a month; a year of verified user history (Shadow, Proof, Ledger) is not. The data moat compounds from day one.

---

*Design phase closed. Next artifact: code.*
