# Splash Artwork – Heartbreak Healer

## Source images (white background)
- `heart_stage1_split.jpg` – heart broken into two halves with glowing gap
- `heart_stage2_cracked.jpg` – whole heart with bright glowing crack
- `heart_stage3_healed.jpg` – heart with crossed bandage (healed)
- `heart_whole.jpg` – solid healed heart (fallback)

## Transparent versions
Generated into `transparent/`:
- heart_stage1_split.png
- heart_stage2_cracked.png
- heart_stage3_healed.png
- heart_healed_fallback.png / heart_whole.png

Background removed with ImageMagick (`-fuzz 10-15% -transparent white`). Soft glows and sparkles preserved. Stage 3 source had a minor JPEG truncation warning; re-processed to full 1600×1600.

## Usage
- System splash (Android 12+): healed heart (stage 3) on white / dark background
- Custom Compose splash: 2.4 s story (broken → cracked → healed + text)
- Launcher / notification icons: prefer user logo in `/branding/logo/` if present; otherwise generate from healed heart

## Gradle task (to be added)
`./gradlew processSplashArt` will regenerate transparent PNGs and density WebPs.
