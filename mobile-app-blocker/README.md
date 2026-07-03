# Mobile App Blocker — Facebook & Instagram

Scripts and instructions to permanently block the Facebook and Instagram apps
on your phone.

> **Honest note up front:** nothing on a device you own and control is 100%
> irreversible — you can always factory-reset or undo a setting. What this
> toolkit does is remove the apps at the system level and then help you put
> enough friction in the way (no USB debugging, a Play Store PIN held by
> someone else) that reinstalling them takes deliberate effort instead of a
> moment of weakness. In practice, that's what makes a block stick.

---

## Android (this is what the scripts are for)

### One-time setup

1. **Install adb** on your computer:
   [Android platform-tools](https://developer.android.com/tools/releases/platform-tools)
   (unzip, and add the folder to your PATH).
2. **Enable USB debugging** on the phone:
   *Settings → About phone → tap "Build number" 7 times*, then
   *Settings → Developer options → USB debugging → ON*.
3. Connect the phone via USB and tap **Allow** on the
   "Allow USB debugging?" prompt.

### Run the block

| Your computer | Command |
|---|---|
| macOS / Linux | `chmod +x block-apps.sh && ./block-apps.sh` |
| Windows | double-click `block-apps.bat` (or run it in Command Prompt) |

The script force-stops each app, removes it from your user profile
(`pm uninstall -k --user 0`), and disables it (`pm disable-user`). Covered
packages: Facebook, Facebook Lite, Instagram, Instagram Lite. Facebook
Messenger (`com.facebook.orca`) is included but commented out — uncomment it
in the script if you want it blocked too.

### Make it stick (important)

Removing the apps is step one. To keep them gone:

1. **Turn USB debugging back OFF** (Developer options → off). This closes the
   door you just used, so the block can't be reversed with the same trick.
2. **Lock the Play Store**: Play Store → Settings → Family →
   **Parental controls** → on → set a PIN → restrict app installs.
   **Give the PIN to someone you trust and don't keep a copy.** This is the
   single most effective step — without it, anything can be reinstalled in
   two taps.
3. Optional extra layer: enable **Digital Wellbeing → Focus mode** or a
   dedicated blocker app, so even the websites are covered (see below).

### Undo (if you ever genuinely need to)

Run `unblock-apps.sh` / `unblock-apps.bat` with USB debugging re-enabled, or
reinstall the apps from the Play Store (requires the parental-control PIN if
you set one — which is the point).

---

## iPhone

A script can't modify an iPhone, but iOS has this built in and it's strong:

1. **Delete the apps** (long-press → Remove App → Delete App).
2. **Screen Time** → *Content & Privacy Restrictions* → ON:
   - *iTunes & App Store Purchases → Installing Apps → Don't Allow*
     (the App Store icon disappears — the apps can't come back).
   - *Content Restrictions → Web Content → Limit Adult Websites → Never
     Allow* → add `facebook.com` and `instagram.com`.
3. **Screen Time → Use Screen Time Passcode** — have someone you trust set
   the passcode so you can't undo it yourself. When asked for an Apple ID
   for passcode recovery, skip it or use theirs.

---

## Blocking the mobile websites (both platforms)

Deleting the apps still leaves `m.facebook.com` and `instagram.com` in the
browser. Two good options:

- **Private DNS (Android, no app needed):** Settings → Network & Internet →
  Private DNS → hostname: `dns.adguard-dns.com` blocks ads/trackers, or use a
  filtering service like NextDNS where you can blocklist
  `facebook.com`, `fbcdn.net`, `instagram.com`, `cdninstagram.com`.
- **iOS:** the Screen Time web-content blocklist in step 2 above already
  covers this.

---

## Files

| File | Purpose |
|---|---|
| `block-apps.sh` / `block-apps.bat` | Block Facebook & Instagram (macOS/Linux / Windows) |
| `unblock-apps.sh` / `unblock-apps.bat` | Reverse the block |
