package com.sina.shopguide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sina.shopguide.R;
import com.sina.shopguide.activity.AboutUsActivity;
import com.sina.shopguide.activity.FeedBackActivity;
import com.sina.shopguide.activity.LoginActivity;
import com.sina.shopguide.dialog.BindAlipayConfirmDlg;
import com.sina.shopguide.util.AppConst;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.UserPreferences;


/**
 * Created by tiger on 18/5/7.
 */

public class MeBottomView extends RelativeLayout {

    private TopPanelView tpvFreshGuide;
    private TopPanelView tpvCommProblen;
    private TopPanelView tpvFeedback;
    private TopPanelView tpvAboutus;
    private BindAlipayConfirmDlg nologinDlg;

    public MeBottomView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MeBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MeBottomView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_me_bottom, this);

        tpvFreshGuide = (TopPanelView) findViewById(R.id.id_fresh_guide);
        tpvCommProblen = (TopPanelView) findViewById(R.id.id_comm_problen);
        tpvFeedback = (TopPanelView) findViewById(R.id.id_feedback);
        tpvAboutus = (TopPanelView) findViewById(R.id.id_aboutus);

        tpvFreshGuide.setOnClickListener(clickListener);
        tpvCommProblen.setOnClickListener(clickListener);
        tpvFeedback.setOnClickListener(clickListener);
        tpvAboutus.setOnClickListener(clickListener);
    }

    private  OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v){
            int id = v.getId();
            switch (id){
                case R.id.id_fresh_guide:
                    ActivityUtils.goWebActivity(getContext(),AppConst.APP_FRESHGUIDE_URL);
                    break;
                case R.id.id_comm_problen:
                    ActivityUtils.goWebActivity(getContext(),AppConst.APP_PROBLEM_URL);
                    break;
                case R.id.id_feedback:
                    if(UserPreferences.isLogin()) {
                        ActivityUtils.goToAdviceActivity(getContext());
                    }else{
                        if(nologinDlg == null){
                            nologinDlg = new BindAlipayConfirmDlg(getContext(), new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityUtils.gotoNextActivity(getContext(), LoginActivity.class);
                                    nologinDlg.dismiss();
                                }
                            },getResources().getString(R.string.toast_not_login2),getResources().getString(R.string.toast_gologin));
                        }

                        nologinDlg.show();
                    }
                    break;
                case R.id.id_aboutus:
                    ActivityUtils.gotoNextActivity(getContext(),AboutUsActivity.class);
                    break;
            }
        }
    };
}
