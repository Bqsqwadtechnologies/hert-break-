# Heartbreak Healer — Google Play policy audit

**Repository:** `Bqsqwadtechnologies/hert-break-`  
**Branch:** `main`  
**Package:** `com.bqsqwad.heartbreakhealer`  
**Version reviewed:** `1.1.0 (versionCode 6)`  
**Audit date:** 25 November 2026

## Scope reviewed

Static source review covered the Android manifest and Gradle configuration, onboarding, navigation/support, local data stores and repositories, journal and unsent-letter screens, memories and trigger screens, SOS/support content, crash diagnostics, notifications, companion promotion, AdMob configuration/manager/consent helper, privacy policy, terms, and GitHub Actions build workflows.

This is a code and documentation review, not a Google Play approval or a review of private Play Console declarations. Play Console metadata and declarations must still be checked by the app owner.

## Changes made on main

- Added an explicit 13+ confirmation to the first onboarding screen. The app does not request a date of birth.
- Hid the AdMob banner while the journal editor is open.
- Hid the AdMob banner while the unsent-letter composer is open.
- Changed “No thanks” and “I'm using it already” on the optional BQ Sqwad AI prompt to dismiss it rather than launching another promotion.
- Removed the behavior where tapping anywhere on the house-promotion video opened an external app/site. Opening the companion now requires a clearly labeled “Learn more” action.
- Added the fuller non-medical wellbeing disclaimer to the in-app Support screen and Terms page.
- Expanded the privacy policy to disclose the local crash report contents and manual sharing behavior, the single-package companion-install check, and the permissions used by the app.
- Updated the README release checklist.

## Code-level observations

- The manifest requests only INTERNET, POST_NOTIFICATIONS, VIBRATE and USE_BIOMETRIC. No broad contacts, location, SMS, call-log, Health Connect, or all-files permission was found in the reviewed manifest.
- Journal entries, unsent letters, mood check-ins, memories, triggers, and journey state are stored locally by the app. The reviewed app does not implement user accounts or a backend upload for this content.
- Google AdMob and UMP are included. Their collection must be reflected in Data safety and the privacy policy; the privacy policy describes device/ad identifiers, IP address or approximate location, ad interactions and diagnostics.
- Crash diagnostics are saved locally and are not automatically uploaded by Heartbreak Healer. A user can choose to copy and share the report.
- No account-creation flow was found, so the account-deletion requirement for account-creating apps is not currently triggered.
- Hearts are app points earned through rewarded ads; the reviewed project README and Terms do not describe paid in-app purchases or cash redemption.

## Required owner actions in Play Console

1. **Health apps declaration:** Play Console → **Monitor & improve → Policy → App content → Health apps**. Complete the declaration accurately because the app contains mental-wellbeing and self-reflection features. Do not describe the app as a medical device or claim that it diagnoses, treats, cures or prevents a condition.
2. **Store listing description:** Add this disclaimer to the full description:  
   “Heartbreak Healer is not a medical device and does not diagnose, treat, cure, or prevent any medical condition. Consult a healthcare professional for medical advice, diagnosis, or treatment.”
3. **Data safety:** Complete the form using the current Google Mobile Ads and UMP SDK data disclosures. Keep local-only app content distinct from information transmitted by advertising SDKs.
4. **Ads declaration:** Declare ads accurately. The app contains AdMob inventory and clearly labeled BQ Sqwad Technologies house promotions.
5. **Target audience:** Set the audience accurately and do not target children under 13. The app now asks users to confirm they are at least 13.
6. **Content rating:** Complete the questionnaire to match the app's actual wellbeing, support, story and advertising content.
7. **Privacy URL:** Enable GitHub Pages with the `/docs` folder and verify that `https://bqsqwadtechnologies.github.io/hert-break-/privacy.html` loads publicly without login or geographic restrictions.
8. **Final review:** Test the release build, privacy/terms links, notification opt-in, consent flow, and ad placements on a physical device.

## Important limitations

- Google Play approval cannot be guaranteed by source changes alone.
- Health apps declaration, Data safety, target audience, content rating, ads declaration, store description, and the public privacy URL are owner-controlled Play Console or hosting settings.
- The latest Debug Build workflow was still pending when this audit note was written. A successful build must be confirmed before release.
