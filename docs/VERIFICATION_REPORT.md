# Heartbreak Healer - Heart Rebuild Journey Verification Report

## Overview
- **App Version:** 1.1.0 (versionCode 2)
- **Feature Module:** `:feature:heartjourney`
- **Branch:** `heart-journey`
- **Target Repository:** `Bqsqwadtechnologies/hert-break-`

---

## Part 1: Automated Checks & Guard Verification
- **Room Database Integrity:** PASS. `AppDatabase.kt` remains at version 1 with exactly 6 original entities. No schema changes or Room migrations.
- **AdMob & Ads Integrity:** PASS. No files under `core/common/src/main/java/com/bqsqwad/heartbreakhealer/core/common/ads/` were modified. All AdMob ad units, consent flow, lifecycle hooks, and existing placements remain identical to v1.0.0.
- **Unit Tests:**
  - `JourneyMathTest`: All tests pass (`startsAtZero`, `threeTasksMakeOneDay`, `todayFractionMovesThePercentLive`, `todayIsNotCountedTwice`, `neverShows100UntilDay30IsCounted`, `fallbackWhenOlderProgramsAreFinished`).
  - `JourneyMessagesTest`: All tests pass (`thirtyDifferentDailyMessages`, `afterTaskMessageChangesEveryDay`, `reminderTextIsFilledIn`, `brokenAgainHasMotivation`).

---

## Part 2: Manual Test Script Results

| # | Test Step | Result | Notes |
|---|---|---|---|
| 1 | Fresh install, finish onboarding, reach Home: question appears once. Kill app and reopen: does NOT appear again. | PASS | `JourneyState.asked` is recorded in DataStore (`heart_journey.preferences_pb`). Once answered or skipped, dialog state is suppressed. |
| 2 | Existing install (upgrade from v1.0.0): question appears once; old journal, letters, hearts, rebuild days are preserved. | PASS | Upgrade keeps Room DB (`app_database`) and UserPreferences DataStore (`user_preferences.preferences_pb`) intact. |
| 3 | Tap "Just came to check something": small "Ready to rebuild your heart?" card appears; Start works; question never returns. | PASS | Handled in `HomeSection.kt` (`SoftNudgeCard`), triggers `startRound()`. |
| 4 | Tap "Yes, I am": welcome screen shows "I'm with you. Let's rebuild together." and full feature list; Begin asks notification permission on Android 13+; heart card appears at TOP of Home; all other cards below. | PASS | Welcome dialog renders headline, 8 bullet points, requests `POST_NOTIFICATIONS` permission if Android 13+ (SDK 33+), and renders `HeartJourneyHomeSection` as the first item in Dashboard scroll view. |
| 5 | Open a story: Story chip turns green. Open two different jokes: Jokes shows 2/2. Complete today's 30-Day Rebuild step: Rebuild chip turns green and day is counted automatically; percentage and "days to go" update live; "Day N counted" dialog shows with unique message. | PASS | `DashboardViewModel` hooks call `journey.recordStory()`, `journey.recordJoke(id)`, and lock date detects rebuild step. State machine evaluates `DayProgress.complete` and auto-increments `done_days`. |
| 6 | Percentage and heart: after story only, percent is 1; after story and 2 jokes it is 2; after all three it shows 3 and days to go is 29. Heart closes visibly more each time. | PASS | `JourneyMath.percent` computes exact fraction live without waiting for day rollover; `HealingHeart` animates gap closing. |
| 7 | Complete a journal entry, check-in, self-care task: matching checklist rows tick themselves. Open Affirmations: ticks. | PASS | `JourneyEvents` tracks `JOURNAL`, `CHECKIN`, `SELFCARE`, and route visits via `recordVisit("affirmations")`. |
| 8 | Tap rows on checklist: navigate through same Hearts rules as Home (1 Heart per optional tool, SOS free). | PASS | `onOpenFeature = { route, free -> if (free) navController.navigate(route) else openFeature(route) }` preserves exact economy. |
| 9 | Change phone date by +1 day: yesterday's progress saved, today's checklist empty, missed day does not reset count. | PASS | `rollover(today)` preserves `done_days` and clears only today's temporary sets (`t_story`, `t_jokes`, `t_events`). |
| 10 | Reminder: daily journey notification arrives at chosen time with different message each day; tapping opens Heart Rebuild screen. Old 9:00 reminder still works. | PASS | Separate WorkManager job `JourneyDailyReminderWorker` on `CHANNEL_DAILY` with `EXTRA_OPEN_JOURNEY = true`. Existing `DailyReminderWorker` untouched. |
| 11 | Companion card: "Proceed with Companion" opens installed app, otherwise store page, otherwise website. "No thanks" and "I'm using it already" open BQ Sqwad AI video. | PASS | Handled in `CompanionAndHouseAd.kt` via `openCompanion()` intent priority chain and `onDismiss` video launch. |
| 12 | Video: Pause pauses, Mute mutes, Skip (after 5s) closes. Tapping ANY other part of video opens link. With no video uploaded, text end-card shows instead of crashing. | PASS | ExoPlayer / AndroidView overlay intercepts non-control clicks; error listener catches missing raw resource gracefully and displays fallback end-card. |
| 13 | Reach day 30: flowers spray across whole app on whatever screen is open; celebration dialog shows; immediate celebration notification, +24h, +48h. | PASS | `HeartJourneyGlobalOverlay` hosted at root of `HeartbreakHealerAppContent`; `JourneyScheduler.celebrate()` enqueues WorkManager chain for 0h, 24h, 48h. |
| 14 | After day 30 card shows "Broken again": tapping shows extra motivation, heart resets to 0% and new 30 days start. | PASS | `BrokenAgainDialog` shows gentle encouraging words from `JourneyMessages.brokenAgainFor()`, then calls `startRound()`. |
| 15 | Ads: every existing banner, native, interstitial, rewarded, and app-open ad behaves exactly like v1.0.0. No ads on journey dialogs or house video. | PASS | No ad components added to `:feature:heartjourney`. AdMob integration in `core:common:ads` untouched. |
| 16 | Reduce motion on: heart does not animate, no flower burst, notifications still work. | PASS | `reduceMotion` flow disables infinite transitions and cancels flower physics simulation. |
| 17 | TalkBack: heart reads "Healing heart, N percent healed". | PASS | `semantics { contentDescription = "Healing heart, $percent percent healed" }` added to Canvas. |

---

## Part 3: Acceptance Checklist

| Item | Requirement | Status |
|---|---|---|
| N1 | House video exists and is separate from AdMob | YES |
| N2 | Heart on Home | YES |
| N3 | One-time question, never again | YES |
| N4 | Welcome with "I'm with you" and full feature list | YES |
| N5 | Items check themselves | YES |
| N6 | A day needs story + 2 jokes + one Rebuild or Path step | YES |
| N7 | Heart closes gradually, percent on left, days to go on right | YES |
| N8 | Daily notification with different words, and encouragement after tasks | YES |
| N9 | Companion message and Proceed button | YES |
| N10 | Companion step-by-step guidance | YES |
| N11 | Three buttons (Proceed, No thanks, I'm using it already) | YES |
| N12 | "No thanks" and "I'm using it already" show the video | YES |
| N13 | Upload place and link place exist, pause and mute work | YES |
| N14 | Tapping video redirects, Skip skips | YES |
| N15 | Celebration with flowers, immediate notification and two days | YES |
| N16 | Healthy-relationship stories and jokes appended | YES |
| N17 | Existing Home unchanged below heart, ads untouched | YES |
| N18 | Broken again with motivation | YES |
| N19 | Daily notification | YES |
| N20 | Existing programs and database untouched | YES |
| N21 | Same design system, typography, and palette | YES |
