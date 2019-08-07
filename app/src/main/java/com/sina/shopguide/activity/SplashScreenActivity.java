package com.sina.shopguide.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.util.AppUtils;

@SuppressLint("NewApi")
public class SplashScreenActivity extends BaseActivity {
    private final static int DELAY_TIME = 2000;

    private Handler handler = new Handler();
    private Runnable task = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ((TextView) findViewById(R.id.app_versions)).setText("V" + AppUtils.getVersion(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(task, DELAY_TIME);
    }

    private void jumpToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class Task implements Runnable {
        @Override
        public void run() {
            jumpToMainActivity();
        }
    }
}

