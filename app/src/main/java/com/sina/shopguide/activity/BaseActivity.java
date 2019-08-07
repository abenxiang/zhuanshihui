package com.sina.shopguide.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.sina.shopguide.R;

public class BaseActivity extends FragmentActivity {

    private boolean reCaclStartime = false;

    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private void registerHomeKeyReceiver(Context context) {
        mHomeKeyReceiver = new HomeWatcherReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private void unregisterHomeKeyReceiver(Context context) {
        if (null != mHomeKeyReceiver) {
            unregisterReceiver(mHomeKeyReceiver);
        }
    }

    public class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)
                        || SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)
                        || SYSTEM_DIALOG_REASON_LOCK.equals(reason)
                        || SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // Home键
                    //CalcInfoUtils.reportCalcInfo(BaseActivity.this);
                    reCaclStartime = true;
                }
            }
        }
    }

    public void logout() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterHomeKeyReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHomeKeyReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void setTitle(String title) {
        ((TextView) findViewById(R.id.title_content)).setText(title);
    }

    protected void setTitleById(int resid) {
        ((TextView) findViewById(R.id.title_content)).setText(resid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void goBackDefault(View v){
        if(v!=null &&  v.getId()==R.id.title_left_button) {
            finish();
        }
    }
}
