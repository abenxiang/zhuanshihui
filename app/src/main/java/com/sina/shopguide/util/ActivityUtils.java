package com.sina.shopguide.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sina.shopguide.activity.FeedBackActivity;
import com.sina.shopguide.activity.MainActivity;
import com.sina.shopguide.activity.WebActivity;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/6/1.
 */

public class ActivityUtils {
    public static void gotoNextActivity(Context context, Class<?> claz){
        Intent in = new Intent(context,claz);
        context.startActivity(in);
    }

    public static void goWebActivity(Context context, String url){
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra(WebActivity.EXTRA_KEY_URL,url);
        context.startActivity(in);
    }

    public static void backToMainActivity(Context context,int indexTab){
        Intent in = new Intent(context, MainActivity.class);
        in.putExtra(MainActivity.MAIN_INDEX,indexTab);
        context.startActivity(in);
    }

    public static void goToAdviceActivity(Context context){
        Intent in = new Intent(context, FeedBackActivity.class);
        context.startActivity(in);
    }

    public static void gotoNextActivity(Context context, Class<?> claz,int extra){
        Intent in = new Intent(context,claz);
        in.putExtra("extra",extra);
        context.startActivity(in);
    }

    public static boolean handleCommonScheme(Context context, String url) {
        if(StringUtils.isEmpty(url)) {
            return false;
        }

        Uri uri = Uri.parse(url);
        if(!uri.getScheme().equals("http") && !uri.getScheme().equals("https")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            try {
                context.startActivity(intent);
                return true;
            }
            catch (ActivityNotFoundException e) {
            }
        }

        return false;
    }
}
