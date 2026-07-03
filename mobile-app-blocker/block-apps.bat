@echo off
REM block-apps.bat — Permanently block Facebook & Instagram on an Android phone (Windows).
REM
REM Requirements:
REM   - USB debugging enabled on the phone (Settings > About phone > tap
REM     "Build number" 7 times, then Developer options > USB debugging).
REM   - adb installed (https://developer.android.com/tools/releases/platform-tools)
REM   - Phone connected via USB and authorized.
REM
REM To reverse the block later, run unblock-apps.bat

setlocal enabledelayedexpansion

where adb >nul 2>&1
if errorlevel 1 (
    echo ERROR: adb is not installed or not on your PATH.
    echo Install Android platform-tools: https://developer.android.com/tools/releases/platform-tools
    exit /b 1
)

adb start-server >nul 2>&1

adb devices | findstr /R /C:"device$" >nul
if errorlevel 1 (
    echo ERROR: No authorized Android device found.
    echo   - Connect your phone via USB.
    echo   - Enable USB debugging.
    echo   - Accept the "Allow USB debugging?" prompt on the phone.
    exit /b 1
)

echo Device found. Blocking apps...
echo.

REM Add com.facebook.orca to also block Facebook Messenger
for %%P in (com.facebook.katana com.facebook.lite com.instagram.android com.instagram.lite) do (
    echo -- Blocking %%P
    adb shell am force-stop %%P >nul 2>&1
    adb shell pm uninstall -k --user 0 %%P >nul 2>&1
    adb shell pm disable-user --user 0 %%P >nul 2>&1
)

echo.
echo Done. Facebook and Instagram are now blocked on this phone.
echo.
echo To make the block stick (recommended):
echo   1. Turn OFF Developer options / USB debugging on the phone.
echo   2. Set up Google Play parental controls and have someone you trust
echo      hold the PIN, so the apps can't be reinstalled.
echo   3. Also block the websites - see README.md.
endlocal
