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

## Ad units to create in AdMob
| Type | Name |
|------|------|
| Banner | `hh_banner_home`, `hh_banner_lists` |
| Native | `hh_native_affirmations`, `hh_native_rebuild` |
| Interstitial | `hh_interstitial_task_complete` |
| Rewarded | `hh_rewarded_hearts` |
| App Open | `hh_app_open` |

Never on: onboarding, journal, SOS, unsent letters, memories, support, settings.

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
