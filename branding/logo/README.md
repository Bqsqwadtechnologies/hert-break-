# Heartbreak Healer — Official Brand Assets

The two uploaded files in this folder are the only approved logo masters:

- `logo.png` — official full-colour healed-heart logo.
- `logo_mono.png` — official monochrome artwork for Android notification and themed-icon use.

## Where they are used

- Animated Compose splash screen: `logo.png` through the shared `HeartbreakHealerLogo` component.
- Dashboard and any other in-app logo using that component: `logo.png`.
- Android system splash icon: `logo.png`.
- Adaptive launcher icon and round launcher icon: `logo.png`, with `logo_mono.png` for Android themed icons.
- Notification small icon (top-left status area): `logo_mono.png`.

## Build integration

Gradle generates Android resources directly from these files. The splash animation, timing, sparkles, and movement are unchanged. Do not embed another copy of the logo in Kotlin source or replace these assets with approximations.

To update the branding later, replace these two files while keeping their filenames.
