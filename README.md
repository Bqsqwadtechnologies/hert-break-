# Heartbreak Healer

**Package:** `com.bqsqwad.heartbreakhealer`  
**Company:** BQ Sqwad Technologies  
**Contact:** bqsqwadtechnologies@bqsqwadai.com

Private, supportive companion for healing after a breakup.

## Current status (main branch only)

- Modular Kotlin + Compose + Material 3 project
- compileSdk / targetSdk **36**, minSdk 26
- Design system, HeartMeter, Onboarding, Dashboard, SOS skeleton, Support screen
- Room + DataStore foundations
- Animated splash story (broken → cracked → healed)
- **Official logo**: glossy red healed heart (used for app icon, notification icon, splash)
- **Monetization**: AdMob only (test IDs). **No Play Billing / no payment SDKs** (per product rules)

## Play Console note
If you see a warning about Play Billing Library 7.1.1 → 8.0.0, it does **not** apply to this app. This project deliberately contains **zero** Billing dependencies. The warning usually comes from another app or an old template. Do not add Billing.

## Logo
See `branding/logo/`. The healed red heart is the source of truth for launcher and notification icons.

## Next
Full SOS, journal, no-contact, rebuild program, hearts shop, AdManager + UMP, notifications, legal pages, CI, signing.
