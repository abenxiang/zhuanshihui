package com.sina.shopguide.activity;

import android.content.Intent;
import android.os.Bundle;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.view.SettingView;

/**
 * Created by tiger on 18/5/8.
 */

public class SettingActivity extends BaseActivity {
    public static final String BUNDLE_USERINFO="USER_INFO";
    private SettingView vwSetting;
    private UserInfo uInfo;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        uInfo = (UserInfo) getIntent().getSerializableExtra(BUNDLE_USERINFO);
        vwSetting  = (SettingView)findViewById(R.id.id_setting);
        vwSetting.update(uInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vwSetting.onActivityResult(requestCode, resultCode, data);
    }
}
