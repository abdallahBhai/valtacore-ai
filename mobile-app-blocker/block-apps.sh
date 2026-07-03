#!/usr/bin/env bash
#
# block-apps.sh — Permanently block Facebook & Instagram on an Android phone.
#
# What it does (per app, for the main user profile):
#   1. Force-stops the app.
#   2. Removes it from your user profile (pm uninstall --user 0), so its icon
#      disappears and it cannot be launched.
#   3. Disables it (pm disable-user) as a second layer in case it was a
#      pre-installed system app that cannot be fully removed.
#
# Requirements:
#   - USB debugging enabled on the phone:
#       Settings > About phone > tap "Build number" 7 times,
#       then Settings > Developer options > enable "USB debugging".
#   - adb installed on this computer (https://developer.android.com/tools/adb)
#   - Phone connected via USB and authorized (accept the prompt on the phone).
#
# Usage:
#   ./block-apps.sh
#
# To reverse the block later, run ./unblock-apps.sh

set -u

PACKAGES=(
  com.facebook.katana      # Facebook
  com.facebook.lite        # Facebook Lite
  com.instagram.android    # Instagram
  com.instagram.lite       # Instagram Lite
  # com.facebook.orca      # Uncomment to also block Facebook Messenger
)

if ! command -v adb >/dev/null 2>&1; then
  echo "ERROR: adb is not installed or not on your PATH."
  echo "Install Android platform-tools: https://developer.android.com/tools/releases/platform-tools"
  exit 1
fi

adb start-server >/dev/null 2>&1

DEVICE_COUNT=$(adb devices | awk 'NR>1 && $2=="device"' | wc -l | tr -d ' ')
if [ "$DEVICE_COUNT" -eq 0 ]; then
  echo "ERROR: No authorized Android device found."
  echo "  - Connect your phone via USB."
  echo "  - Enable USB debugging (see header of this script)."
  echo "  - Accept the 'Allow USB debugging?' prompt on the phone."
  exit 1
fi

echo "Device found. Blocking apps..."
echo

for PKG in "${PACKAGES[@]}"; do
  if adb shell pm list packages --user 0 2>/dev/null | grep -q "^package:${PKG}$"; then
    echo "── Blocking ${PKG}"
    adb shell am force-stop "$PKG" >/dev/null 2>&1
    adb shell pm uninstall -k --user 0 "$PKG" >/dev/null 2>&1 \
      && echo "   removed from your profile" \
      || echo "   could not remove (may be a protected system app)"
    adb shell pm disable-user --user 0 "$PKG" >/dev/null 2>&1 \
      && echo "   disabled"
  else
    echo "── ${PKG}: not installed, skipping"
  fi
done

echo
echo "Done. Facebook and Instagram are now blocked on this phone."
echo
echo "To make the block stick (recommended):"
echo "  1. Turn OFF Developer options / USB debugging on the phone, so this"
echo "     process can't be trivially reversed."
echo "  2. Set up Google Play parental controls (Play Store > Settings >"
echo "     Family > Parental controls) and have someone you trust hold the"
echo "     PIN — this prevents reinstalling the apps from the Play Store."
echo "  3. Also block the websites: see README.md, section 'Blocking the"
echo "     mobile websites'."
