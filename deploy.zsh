#!/usr/bin/env zsh

# /* configuration */
export PATH="$PATH:$HOME/Android/Sdk/platform-tools/"
APK_PATH='./app/build/outputs/apk/release/app-release-unsigned.apk'
DEPLOY_PATH='/data/local/tmp/Anti-EarSpy-PoC.apk'
MAIN_CLASS='anti.earspy.poc.MainKt'

# /* build apk */
rm -f $APK_PATH
./gradlew aRelease

# /* deploy to device */
adb push "$APK_PATH" "$DEPLOY_PATH"

# /* run program */
adb shell app_process -Djava.class.path="$DEPLOY_PATH" /system/bin "$MAIN_CLASS"
