#!/usr/bin/env bash

echo "install emulator"

# Install AVD files
echo "y" | $ANDROID_HOME/tools/bin/sdkmanager --install 'system-images;android-28;google_apis_playstore;x86'

# Create emulator
echo "no" | $ANDROID_HOME/tools/bin/avdmanager create avd -n xamarin_android_emulator -k 'system-images;android-28;google_apis_playstore;x86' --force

$ANDROID_HOME/emulator/emulator -list-avds