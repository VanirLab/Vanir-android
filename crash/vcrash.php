<?php
 $dt = new DateTime('now', new DateTimeZone('Europe/Amsterdam'));
 // save the log to a filename with MMDDHHMMSS such as: "1123132456.txt"
 $orderday = substr($dt->format('Y-m-d H:i:s'),8,2);
 $ordermon = substr($dt->format('Y-m-d H:i:s'),5,2);
 $ordertim = substr($dt->format('Y-m-d H:i:s'),11,2) . substr($dt->format('Y-m-d H:i:s'),14,2)
 . substr($dt->format('Y-m-d H:i:s'),17,2);
 $fname = $ordermon . $orderday . $ordertim . ".txt";
 $FileLog = $_SERVER['DOCUMENT_ROOT'] . "/vanircrashlogs/" . $fname;
 $HandleLog = fopen($FileLog, 'a');
 fwrite($HandleLog, "REPORT_ID=" . $_POST['REPORT_ID'] . "\r\n");
 fwrite($HandleLog, "APP_VERSION_CODE=" . $_POST['APP_VERSION_CODE'] . "\r\n");
 fwrite($HandleLog, "APP_VERSION_NAME=" . $_POST['APP_VERSION_NAME'] . "\r\n");
 fwrite($HandleLog, "PACKAGE_NAME=" . $_POST['PACKAGE_NAME'] . "\r\n");
 fwrite($HandleLog, "PHONE_MODEL=" . $_POST['PHONE_MODEL'] . "\r\n");
 fwrite($HandleLog, "ANDROID_VERSION=" . $_POST['ANDROID_VERSION'] . "\r\n");
 fwrite($HandleLog, "STACK_TRACE=" . $_POST['STACK_TRACE'] . "\r\n");
 fwrite($HandleLog, "TOTAL_MEM_SIZE=" . $_POST['TOTAL_MEM_SIZE'] . "\r\n");
 fwrite($HandleLog, "AVAILABLE_MEM_SIZE=" . $_POST['AVAILABLE_MEM_SIZE'] . "\r\n");
 fwrite($HandleLog, "DISPLAY=" . $_POST['DISPLAY'] . "\r\n");
 fwrite($HandleLog, "USER_APP_START_DATE=" . $_POST['USER_APP_START_DATE'] . "\r\n");
 fwrite($HandleLog, "USER_CRASH_DATE=" . $_POST['USER_CRASH_DATE'] . "\r\n");
 fwrite($HandleLog, "LOGCAT=" . $_POST['LOGCAT'] . "\r\n");
 fwrite($HandleLog, "DEVICE_ID=" . $_POST['DEVICE_ID'] . "\r\n");
 fwrite($HandleLog, "SHARED_PREFERENCES=" . $_POST['SHARED_PREFERENCES'] . "\r\n");
 fclose($HandleLog);
 fclose($Handle);
?>
