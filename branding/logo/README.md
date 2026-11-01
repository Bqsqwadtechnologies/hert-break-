# Branding / Logo – Heartbreak Healer

## Official logo (user-provided)
- `logo.png` / `logo_1024.png` – glossy red healed heart (transparent background)
- `logo_mono.png` – single-color silhouette for monochrome adaptive icon & notification small icon

This healed heart is the **app icon**, **notification icon**, and **system splash icon**.

The earlier blue glowing heart (if present) is superseded by this red healed heart per user request (1 Nov 2026).

## Automatic generation (to be completed)
A Gradle task + GitHub Action will generate from `logo.png`:
- Adaptive launcher (foreground + background + themed monochrome)
- Legacy mipmaps
- Notification small icon (white silhouette)
- Splash icon (healed heart)
- 512 px Play Store icon

Until generated resources exist, the app uses placeholders that point to the healed heart assets.

## Splash stages
See `/branding/splash/` for the broken → cracked → healed animation assets. Stage 3 now uses this same healed heart.
