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

Private feature landing/list screens may show a clearly-labelled banner when the user is not editing private content. Ads are not shown inside journal/letter/memory editors, Urge SOS, support/crisis information, onboarding, privacy or legal screens; Settings only has a separated footer placement. Rewarded videos are voluntary; Hearts are credited only by the Google Mobile Ads reward callback. Each optional feature visit costs 1 Heart; Urge SOS, support and Settings remain free. If a user lacks Hearts, the app opens Hearts Shop and returns to the requested feature after a completed rewarded view.

## Legal
- Privacy: https://bqsqwadtechnologies.github.io/hert-break-/privacy.html
- Terms: https://bqsqwadtechnologies.github.io/hert-break-/terms.html  
Enable GitHub Pages from `/docs`.

## Build
```bash
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease
```

## Google Play release and policy checklist
1. Confirm release AdMob app/unit IDs are registered to this app; debug builds must continue using Google's official test IDs.
2. Generate/verify the existing release keystore and GitHub signing secrets. Never commit signing material.
3. Enable GitHub Pages using the `/docs` folder and verify the privacy page is publicly accessible:
   `https://bqsqwadtechnologies.github.io/hert-break-/privacy.html`
4. Complete the Health apps declaration in Play Console because the app includes mental-wellbeing and self-reflection features. Do not claim diagnosis, treatment, cure, prevention, or medical-device functionality.
5. Add the required health disclaimer to the Play Store description: “Heartbreak Healer is not a medical device and does not diagnose, treat, cure, or prevent any medical condition. Consult a healthcare professional for medical advice, diagnosis, or treatment.”
6. Complete Data safety using the current AdMob/UMP SDK disclosures. The app's journal, letters, mood check-ins and journey content stay on-device and are not uploaded by Heartbreak Healer; Google advertising SDK data is separately disclosed in the privacy policy.
7. Declare that the app contains ads (AdMob and clearly labeled BQ house promotions).
8. Set the target audience accurately and do not target children under 13. Onboarding now asks users to confirm they are at least 13.
9. Complete the content-rating questionnaire accurately, including the app's wellbeing/support content and advertising.
10. Confirm the in-app Privacy Policy and Terms links work, Play App Signing is configured, then upload the signed AAB.
11. Run the full release build and test the installed release build on a physical device before submitting.

Repo: https://github.com/Bqsqwadtechnologies/hert-break-
