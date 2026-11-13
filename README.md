# Heartbreak Healer

**Package:** `com.bqsqwad.heartbreakhealer`  
**Company:** BQ Sqwad Technologies  
**Contact:** bqsqwadtechnologies@bqsqwadai.com  
**Version:** 1.0.0 (versionCode 1)

Private, supportive companion for healing after a breakup.

## Tech
- Kotlin, Jetpack Compose, Material 3
- Hilt, Room, DataStore, Navigation, WorkManager
- compileSdk / targetSdk **36**, minSdk **26**
- AdMob only (no Play Billing, no accounts)

## Implemented features
- Onboarding (nickname, goals)
- Animated splash (broken → cracked → healed)
- Dashboard + HeartMeter + Daily check-in
- Full Urge SOS (breathe, 5-min timer, grounding, reasons, don’t-text)
- Support / crisis screen (required wording)
- Private Journal + Unsent Letters (release/burn)
- No-Contact streak + gentle slip restart
- Self-Care planner
- 30-Day Rebuild program
- Affirmations (40+ offline + “today”)
- Hearts Shop + HeartsRepository (earn/spend)
- Memories Box
- Triggers list
- Settings (notifications, reduce motion, clear data, privacy/terms)
- UserPreferences DataStore + Room entities/DAOs/repos
- AdManager + AdsConfig + AdPlacementMap + UMP ConsentHelper
- DailyReminderWorker + NotificationScheduler
- Journal biometric lock helper
- Privacy Policy + Terms (`/docs`)
- CI (debug + release workflows)

## AdMob inventory and placements

All 16 production ad-unit IDs are centralized in `core/common/.../ads/AdsConfig.kt`.
Debug builds use Google's official test ad units; release builds use the configured production units.

| Format | Production placements |
|------|------|
| Banner (5) | Home, general lists, private/library footer, 14-day path, Hearts Shop |
| Native (5) | Affirmations, 30-Day Rebuild, Healing Library, 14-day path, Weekly Review |
| Interstitial (3) | Self-Care completion, path-day completion, Weekly Review exit |
| Rewarded (2) | Earn 15 Hearts, optional extra 15 Hearts |
| App Open (1) | Eligible return after the configured background interval |

Private feature landing/list screens may show a clearly-labelled banner when the user is not editing private content. Ads are not shown inside journal/letter/memory editors, Urge SOS, support/crisis information, onboarding, settings, privacy or legal screens. Rewarded videos are voluntary; Hearts are credited only by the Google Mobile Ads reward callback. Each optional feature visit costs 1 Heart; Urge SOS, support and Settings remain free. If a user lacks Hearts, the app opens Hearts Shop and returns to the requested feature after a completed rewarded view.

## Legal
- Privacy: https://bqsqwadtechnologies.github.io/hert-break-/privacy.html
- Terms: https://bqsqwadtechnologies.github.io/hert-break-/terms.html  
Enable GitHub Pages from `/docs`.

## Build
```bash
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease
```

## Release checklist
1. Replace test AdMob IDs in `AdsConfig` + Manifest
2. Generate release keystore + GitHub secrets
3. Enable GitHub Pages for `/docs`
4. Data safety: on-device data, AdMob, no accounts
5. Content rating 13+
6. Play App Signing
7. Upload AAB

Repo: https://github.com/Bqsqwadtechnologies/hert-break-
