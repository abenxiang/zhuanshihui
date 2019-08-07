package com.sina.shopguide.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.sina.shopguide.R;
import com.sina.shopguide.dialog.BindAlipayConfirmDlg;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.TopPanelTipView;

/**
 * Created by tiger on 18/5/8.
 */

public class AboutUsActivity extends BaseActivity {

    private TopPanelTipView tptvVersion;
    private TopPanelTipView tptvPinfen;
    private TopPanelTipView tptvFeedback;
    private BindAlipayConfirmDlg nologinDlg;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        tptvVersion  = (TopPanelTipView)findViewById(R.id.id_curr_version);
        tptvPinfen  = (TopPanelTipView)findViewById(R.id.id_pinfen);
        tptvFeedback  = (TopPanelTipView)findViewById(R.id.id_feedbk);
        setTitleById(R.string.aboutus_title);
        tptvVersion.setTip(AppUtils.getVersion(this));
        tptvPinfen.setOnClickListener(clicklistener);
        tptvFeedback.setOnClickListener(clicklistener);
    }

    private View.OnClickListener clicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.id_pinfen:
                    gotoMarcket();
                    break;
                case R.id.id_feedbk:
                    if(UserPreferences.isLogin()) {
                        ActivityUtils.goToAdviceActivity(AboutUsActivity.this);
                    }else{
                        if(nologinDlg == null){
                            nologinDlg = new BindAlipayConfirmDlg(AboutUsActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityUtils.gotoNextActivity(AboutUsActivity.this, LoginActivity.class);
                                    nologinDlg.dismiss();
                                }
                            },getResources().getString(R.string.toast_not_login2),getResources().getString(R.string.toast_gologin));
                        }

                        nologinDlg.show();
                    }
                    break;
            }
        }
    };

    private void gotoMarcket(){
        try{
            Uri uri = Uri.parse("market://details?id="+getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch(Exception e){
            ToastUtils.showToast(AboutUsActivity.this, "您的手机没有安装Android应用市场");
            e.printStackTrace();
        }
    }
}
