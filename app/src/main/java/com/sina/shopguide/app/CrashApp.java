package com.sina.shopguide.app;

import com.mob.MobApplication;

public class CrashApp extends MobApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    Thread.currentThread().setUncaughtExceptionHandler(CrashHandler.getInstance());
  }

}
