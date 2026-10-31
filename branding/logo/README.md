# Branding / Logo

Place the master logo here as `logo.png` (1024×1024, preferably with transparency).

Optional: `logo_mono.png` (single-color silhouette for monochrome adaptive icon / notification).

A Gradle task (and GitHub Action) will automatically generate:

- Adaptive launcher icons (foreground, background, themed monochrome)
- Legacy mipmaps
- Notification small icon (white silhouette)
- Splash screen icon
- In-app header logo
- 512px Play Store icon

Until the generated resources exist, the app uses a clean placeholder. All app code reads from the generated resources so replacing the logo updates everything without code changes.

Current logo: provided by user (blue glowing heart).
