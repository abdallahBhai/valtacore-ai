#!/usr/bin/env bash
#
# unblock-apps.sh — Reverse the block applied by block-apps.sh.
#
# Re-enables and reinstalls (for your user profile) Facebook & Instagram.
# Requires the same setup as block-apps.sh: adb + USB debugging.

set -u

PACKAGES=(
  com.facebook.katana
  com.facebook.lite
  com.instagram.android
  com.instagram.lite
  # com.facebook.orca
)

if ! command -v adb >/dev/null 2>&1; then
  echo "ERROR: adb is not installed or not on your PATH."
  exit 1
fi

adb start-server >/dev/null 2>&1

for PKG in "${PACKAGES[@]}"; do
  echo "── Restoring ${PKG}"
  adb shell pm enable --user 0 "$PKG" >/dev/null 2>&1
  adb shell cmd package install-existing --user 0 "$PKG" >/dev/null 2>&1 \
    && echo "   restored" \
    || echo "   not restorable via adb — reinstall it from the Play Store"
done

echo
echo "Done. Any app that couldn't be restored can be reinstalled from the Play Store."
