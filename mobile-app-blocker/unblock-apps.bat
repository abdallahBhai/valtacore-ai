@echo off
REM unblock-apps.bat — Reverse the block applied by block-apps.bat.

where adb >nul 2>&1
if errorlevel 1 (
    echo ERROR: adb is not installed or not on your PATH.
    exit /b 1
)

adb start-server >nul 2>&1

for %%P in (com.facebook.katana com.facebook.lite com.instagram.android com.instagram.lite) do (
    echo -- Restoring %%P
    adb shell pm enable --user 0 %%P >nul 2>&1
    adb shell cmd package install-existing --user 0 %%P >nul 2>&1
)

echo.
echo Done. Any app that couldn't be restored can be reinstalled from the Play Store.
