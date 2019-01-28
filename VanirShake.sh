#!/bin/bash
# Utility script to run VanirShake
#
# See: https://developer.android.com/studio/test/monkey.html
rm tmp/VanirShake.log
adb shell monkey -p app.vanir --throttle 100 -s 43686 -v 50000 |
 tee tmp/VanirShake.log

