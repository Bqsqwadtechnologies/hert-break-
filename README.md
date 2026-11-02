# Heartbreak Healer

**Package:** `com.bqsqwad.heartbreakhealer`  
**Company:** BQ Sqwad Technologies  
**Contact:** bqsqwadtechnologies@bqsqwadai.com  
**Version:** 1.0.0 (versionCode 1)

Private, supportive companion that helps people get through a breakup day by day.

## Tech
- Kotlin, Jetpack Compose, Material 3
- MVVM + Clean Architecture, Hilt, Room, DataStore, Navigation, WorkManager
- compileSdk / targetSdk **36**, minSdk **26**
- AdMob only (no Play Billing, no accounts)

## Logo & Splash
- Official logo: glossy red healed heart (`branding/logo/`)
- Animated splash: broken → cracked → healed (~2.4 s first launch)
- System splash uses healed heart

## Ads (test IDs active)
See `AdsConfig.kt` and `AdPlacementMap.kt`.

**Create these units in AdMob:**
| Type | Name |
|------|------|
| Banner | `hh_banner_home`, `hh_banner_lists` |
| Native | `hh_native_affirmations`, `hh_native_rebuild` |
| Interstitial | `hh_interstitial_task_complete` |
| Rewarded | `hh_rewarded_hearts` |
| App Open | `hh_app_open` |

Never show ads on: onboarding, journal, SOS, unsent letters, memories, support/crisis, settings.

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
1. Replace test AdMob IDs in `AdsConfig` + Manifest with real ones
2. Ensure final logo is in `branding/logo/logo.png`
3. Generate release keystore and store secrets in GitHub Actions
4. Enable GitHub Pages for `/docs`
5. Data safety form: data stays on device, AdMob, no accounts
6. Content rating: 13+
7. Enroll in Play App Signing
8. Upload AAB

## Signing
```bash
keytool -genkey -v -keystore heartbreak-healer-release.jks -keyalg RSA -keysize 4096 -validity 100000 -alias heartbreak_healer
```
Secrets: `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.
Never commit the keystore.
