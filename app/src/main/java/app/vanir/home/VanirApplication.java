package app.vanir.ui;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.multidex.MultiDex;

import org.acra.ACRA;

import org.acra.annotation.AcraMailSender;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraHttpSender;
import org.acra.annotation.AcraNotification;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.config.NotificationConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

import app.vanir.core.System;
import app.vanir.BuildConfig;

import app.vanir.service.Services;
import app.vanir.R;
import android.app.Application;



import java.net.NoRouteToHostException;

@AcraHttpSender(
  httpMethod = HttpSender.Method.PUT,
  uri = "[LINK YOUR URI UPDATE REPORT]",
  basicAuthLogin = "numbersAndLetters",
  basicAuthPassword = "1234",
  connectionTimeout = 5000
)
@AcraNotification (
        resChannelName = R.string.ChannelId,
        resText = R.string.crash_dialog_text,
        resIcon = R.mipmap.ic_launcher_foreground_vanirlogo,
        resTitle = R.string.crash_dialog_title,
        resCommentPrompt = R.string.crash_dialog_comment
)


@AcraCore(
        applicationLogFile = "/vanirtd.log",
        buildConfigClass = BuildConfig.class,
        reportFormat = StringFormat.JSON,
        sendReportsInDevMode = true,
        deleteOldUnsentReportsOnApplicationStart = true
)

public class VanirApplication extends Application {

  public static final String LOG_TAG = "vanir";

  @Override
  public void onCreate() {


    SharedPreferences themePrefs = getSharedPreferences("THEME", 0);
    Boolean isDark = themePrefs.getBoolean("isDark", false);
    if (isDark)
      setTheme(R.style.DarkTheme);
    else
      setTheme(R.style.AppTheme);

    super.onCreate();

    ACRA.init(this);
    Services.init(this);

    // initialize the system
    try {
      System.init(this);
    } catch (Exception e) {
      // ignore exception when the user has wifi off
      if (!(e instanceof NoRouteToHostException))
        System.errorLogging(e);
    }

    CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
    builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
    builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class);
    builder.getPluginConfigurationBuilder(NotificationConfigurationBuilder.class);
    ACRA.init(this, builder);

  }


  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
      MultiDex.install(this);
    ACRA.init(this);
  }




}
