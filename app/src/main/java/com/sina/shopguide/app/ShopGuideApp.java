package com.sina.shopguide.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.sina.shopguide.util.AlibcUtils;
import com.sina.shopguide.util.AndfixProcessor;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.ImageLoaderUtils;

/**
 * Created by tiger on 18/4/24.
 */

public class ShopGuideApp extends CrashApp {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.initAppContext(getApplicationContext());
        ImageLoaderUtils.initImageLoader();
        //AndfixProcessor.init(getApplicationContext());
        AlibcUtils.initAlibcSdk(this);
    }
}
