# Heartbreak Healer

**Package:** `com.bqsqwad.heartbreakhealer`  
**Company:** BQ Sqwad Technologies  
**Contact:** bqsqwadtechnologies@bqsqwadai.com

A private, supportive companion that helps people get through a breakup day by day, with tools to heal, stay strong and move forward.

## Status (2026-10-31)

- Repository initialized
- Feature branch: `feature/initial-scaffold`
- Modular structure defined (app + core + feature modules)
- Design system started (blue/white light theme, midnight navy/plum dark theme, soft heart-pink accent)
- Logo placed in `/branding/logo/logo.png` (user-provided glowing heart)
- Basic Application, MainActivity, Navigation graph, themes, strings
- Version catalog with AGP 9.5.0, Kotlin 2.4.21, Compose, Hilt, Room, DataStore, AdMob, etc.
- compileSdk / targetSdk 36, minSdk 26

### Next steps in progress
1. Complete core:data (Room + DataStore + encrypted hearts, journal, streaks)
2. Implement heart meter, hearts economy, and shop
3. Onboarding flow
4. Dashboard with healing meter
5. SOS, No-Contact, Journal, Unsent Letters, Rebuild program, etc.
6. AdManager + UMP consent
7. Notifications + WorkManager
8. Logo pipeline (Gradle task + Action) to generate all icons from branding/logo
9. Legal pages (GitHub Pages)
10. Signing + CI workflows
11. Full polish, tests, Play Store assets

## Tech stack
- Kotlin, Jetpack Compose, Material 3
- MVVM + Clean Architecture
- Hilt, Room, DataStore, WorkManager, Navigation Compose
- Google AdMob (test IDs), UMP
- Offline-first, no accounts, local only

## Workspace rule
All work happens inside this repository on feature branches. Merge to main only when the build is green.

## Building
Once Android SDK is available:
```bash
./gradlew :app:assembleDebug
```

## Logo
See `branding/logo/README.md`. The master logo is already present.
