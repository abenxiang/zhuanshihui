package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.activity.BindAlipayActivity;
import com.sina.shopguide.activity.InvateParnerActivity;
import com.sina.shopguide.activity.MyGroupActivity;
import com.sina.shopguide.activity.ProfitManageActivity;
import com.sina.shopguide.activity.SettingActivity;
import com.sina.shopguide.activity.WithDrawActivity;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.util.SpannableFormatUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class MeCenterView extends RelativeLayout implements IUpdate<UserInfo>{
    private TextView tvDrawTotal;
    private TextView tvDrawOpr;

    private VerDoubleTextView vdtvMonthEarn;
    private VerDoubleTextView vdtvTodayEarn;
    private VerDoubleTextView vdtvMygroup;

    private VerImageTextView vitvEarn;
    private VerImageTextView vitvGroup;
    private VerImageTextView vitvInvate;
    private UserInfo uInfo;

    public MeCenterView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MeCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MeCenterView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_me_center, this);
        tvDrawOpr = (TextView) findViewById(R.id.id_draw_opr);
        tvDrawTotal = (TextView) findViewById(R.id.id_draw_total);

        vdtvMonthEarn = (VerDoubleTextView) findViewById(R.id.id_month_earn);
        vdtvTodayEarn = (VerDoubleTextView) findViewById(R.id.id_today_earn);
        vdtvMygroup = (VerDoubleTextView) findViewById(R.id.id_mygroup);

        vitvEarn = (VerImageTextView) findViewById(R.id.id_earn);
        vitvGroup = (VerImageTextView) findViewById(R.id.id_group);
        vitvInvate = (VerImageTextView) findViewById(R.id.id_invate);

        vitvEarn.setOnClickListener(clickListener);
        vitvGroup.setOnClickListener(clickListener);
        vitvInvate.setOnClickListener(clickListener);
        tvDrawOpr.setOnClickListener(clickListener);
    }

    public void update(UserInfo info) {
        if (info == null) {
            return;
        }
        uInfo = info;

        System.out.println("ttttttthhhh isparner:"+uInfo.is_partner());
        vdtvMonthEarn.setLocalEnable(uInfo.is_partner());
        vdtvTodayEarn.setLocalEnable(uInfo.is_partner());
        vdtvMygroup.setLocalEnable(uInfo.is_partner());
        vitvEarn.setLocalEnable(uInfo.is_partner());
        vitvGroup.setLocalEnable(uInfo.is_partner());
        vitvInvate.setLocalEnable(uInfo.is_partner());
        if(uInfo.is_partner()){
            tvDrawOpr.setBackgroundResource(R.drawable.bg_text_bigcorner_red);
            tvDrawTotal.setTextColor(getResources().getColor(R.color.colorRedTitle));
        }else{
            tvDrawOpr.setBackgroundResource(R.drawable.bg_text_bigcorner_gray);
            tvDrawTotal.setTextColor(getResources().getColor(R.color.colorTopPanelTip));
        }

        vdtvMonthEarn.setTip(uInfo.getMonth_commision());
        vdtvTodayEarn.setTip(uInfo.getDay_commision());
        vdtvMygroup.setTip(uInfo.getSubordinate_user_sum());

        tvDrawTotal.setText(SpannableFormatUtils.toEarnPriceSpanable(Float.parseFloat(uInfo.getAvailable_cash())));
    }

    private  OnClickListener clickListener = new OnClickListener(){
        public void onClick(View v){
            int id = v.getId();
            switch (id){
                case R.id.id_earn:
                    Intent in = new Intent(getContext(), ProfitManageActivity.class);
                    in.putExtra(ProfitManageActivity.USER_INF,uInfo);
                    getContext().startActivity(in);
                    break;
                case R.id.id_group:
                    goNextActivity(MyGroupActivity.class);
                    break;
                case R.id.id_invate:
                    goInvateParner();
                    break;
                case R.id.id_draw_opr:
                    goDraw();
                    break;
            }
        }
    };

    private void goNextActivity(Class<?> claz){
        Intent in = new Intent(getContext(), claz);
        getContext().startActivity(in);
    }

    private void goDraw(){
        Intent in = new Intent(getContext(), WithDrawActivity.class);
        in.putExtra(ProfitManageActivity.USER_INF,uInfo);
        getContext().startActivity(in);
    }


    private void goInvateParner(){
        Intent in = new Intent(getContext(), InvateParnerActivity.class);
        in.putExtra(SettingActivity.BUNDLE_USERINFO,uInfo);
        getContext().startActivity(in);
    }
}
