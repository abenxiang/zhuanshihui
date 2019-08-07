package com.sina.shopguide.app;

import android.os.Process;
import android.util.Log;

import com.sina.shopguide.util.FileUtils;


public class CrashHandler implements Thread.UncaughtExceptionHandler {

  private final static String TAG = CrashHandler.class.getName();

  private static CrashHandler instance;

  private CrashHandler() {}

  public synchronized static CrashHandler getInstance() {
    if (instance == null) {
      instance = new CrashHandler();
      Thread.setDefaultUncaughtExceptionHandler(instance);
    }
    return instance;
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    Log.e(TAG, "handle uncaughtException ", ex);
    FileUtils.makeCrashReport(ex);
    Process.killProcess(Process.myPid());
    System.exit(1);
  }

}
