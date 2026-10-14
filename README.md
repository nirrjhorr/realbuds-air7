# RealBuds Air 7

An open-source, private Android companion app specifically tailored and optimized for the **realme Buds Air 7**, communicating directly over the earbuds' native Bluetooth protocol.

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android%208.0%2B-brightgreen.svg)](#requirements)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.21-7f52ff.svg)](https://kotlinlang.org)

---

## Special Thanks & Acknowledgements

This project is a dedicated adaptation and improvement built upon the work of **Rafsan Basunia ([@rafsanbasunia](https://github.com/rafsanbasunia))** on the original [**realbuds-oss**](https://github.com/rafsanbasunia/realbuds-oss) project.

Huge gratitude to Rafsan for reverse-engineering the realme Bluetooth SPP protocol, creating the core framing engine, and laying the groundwork. This fork builds directly upon his foundation to optimize, refine, and tailor the experience specifically for the **realme Buds Air 7**.

---

## Why RealBuds Air 7?

The official realme Link app is bloated, requires an online account, collects telemetry, and hides or misconfigures several hardware capabilities.

**RealBuds Air 7** talks directly to your earbuds over Bluetooth Classic SPP:
- **100% Offline & Private:** Declares **zero internet permissions** (`android.permission.INTERNET` is completely absent). It cannot send your data anywhere.
- **No Account Required:** No logins, no tracking, no bloat.
- **Instant Response:** Fast, native Android Jetpack Compose UI with dynamic theming (Light, Dark, and Material You dynamic colors).

---

## What's New & Changelog (Buds Air 7 Improvements)

Compared to the upstream base prototyped for Buds Air 8, this build features complete hardware calibration and UI tailoring for the **realme Buds Air 7**:

### 1. Accurate Hardware Feature Filtering
- Queried and matched against the exact Buds Air 7 firmware feature mask (`Cmd.FEATURE_LIST` / `0x810D`).
- Only genuine hardware-supported toggles appear (Game mode, Spatial audio, High volume mode, Wind noise reduction, Touch tones, Find my earbuds, etc.).
- Completely removed dead/unsupported controls and phantom cards.

### 2. Calibrated ANC & Ambient Sound
- Aligned the ANC wire protocol to device-captured packet traces (wire code `1` for **Off**, `2` for **Transparency**).
- Tuned cancellation strengths to the 3 hardware-supported levels: **Smart (Adaptive)**, **Max**, and **Moderate** (removed non-existent Mild mode).

### 3. Redesigned Gesture Controls
- Rebuilt the Controls tab into an expandable vertical accordion.
- Filtered gestures to the 3 hardware-supported actions:
  - **Double tap**
  - **Triple tap**
  - **Touch & hold (1s)**
- **Independent Earbud Bindings:** Removed the redundant "Both earbuds" mirroring card and switch. The screen cleanly starts with **Left earbud** followed by **Right earbud**, allowing completely independent gesture actions per side.

### 4. Clean, Human-Friendly Error Dialogs
- **Bluetooth Off Dialog:** Removed duplicate text; now shows a clean, single-line instruction to enable Bluetooth in settings, omitting unnecessary "Copy log" buttons.
- **Connection Fallback:** Replaced intimidating raw RFCOMM socket exceptions (`read failed, socket might closed or timeout`) with a friendly *"Device unavailable: Make sure your realme Buds are out of the case and nearby"* prompt.

### 5. Production Release & Size Optimization
- Added tailored ProGuard / R8 bytecode rules protecting Bluetooth models and Compose states while stripping unused dependencies.
- **Shrunk APK size from ~15.9 MB down to ~1.2 MB**.
- Fully signed with a dedicated release keystore.

---

## Download & Installation

Grab the latest pre-compiled signed release from the [Releases](../../releases) tab or download the direct APKs from the build output:

| Variant | Recommended For | Size |
|---|---|---|
| **Universal APK** | Any device (if you are unsure) | ~1.25 MB |
| **ARM64 (arm64-v8a)** | Modern Android phones (2017+) | ~1.20 MB |
| **ARM32 (armeabi-v7a)** | Older 32-bit Android phones | ~1.19 MB |

---

## Requirements

- Android 8.0 (API 26) or higher.
- realme Buds Air 7 paired via system Bluetooth.
- Standard Bluetooth permissions (`BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`).
- *(Optional)* Physical step detector permission for Adaptive ANC motion switching.

---

## Building from Source

```bash
# Clone this repository
git clone https://github.com/nirrjhorr/realbuds-air7.git
cd realbuds-air7

# Build release APKs
./gradlew assembleRelease
```

The signed release APKs will be generated in:
```text
app/build/outputs/apk/release/
```

---

## License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**. See the [LICENSE](LICENSE) file for details.

- Original RealBuds Copyright (C) 2024-2026 Rafsan Basunia
- realme Buds Air 7 Port & Improvements Copyright (C) 2026 nirrjhorr

---

## Disclaimer

This is an independent open-source utility developed for interoperability. It is not affiliated with, endorsed by, or connected to realme or Shenzhen OnePlus Technology Co., Ltd. "realme" and "Buds Air" are trademarks of their respective owners.
