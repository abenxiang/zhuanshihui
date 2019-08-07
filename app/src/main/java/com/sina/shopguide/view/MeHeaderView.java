package com.sina.shopguide.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.LoginActivity;
import com.sina.shopguide.activity.SettingActivity;
import com.sina.shopguide.dialog.BindAlipayConfirmDlg;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.ImageLoaderUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.util.UserPreferences;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class MeHeaderView extends RelativeLayout implements IUpdate<UserInfo> {
    private TextView tvLogin;
    private ImageView ivSet;
    private CircleImageView ivAvatar;

    private RelativeLayout rvLoginMore;
    private TextView tvLoginNick;
    private TextView tvLoginInvate;
    private TextView tvLoginCopy;
    private UserInfo uInfo;

    private BindAlipayConfirmDlg nologinDlg;
    public MeHeaderView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MeHeaderView(Context context) {
        super(context);
        init(null);
    }

    public void adjustView(boolean isLogin){
        if(isLogin){
            tvLogin.setVisibility(View.INVISIBLE);
            rvLoginMore.setVisibility(View.VISIBLE);
        }else{
            tvLogin.setVisibility(View.VISIBLE);
            rvLoginMore.setVisibility(View.INVISIBLE);
            ivAvatar.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_me_header, this);
        tvLogin = (TextView) findViewById(R.id.id_login);
        ivSet = (ImageView) findViewById(R.id.id_sets);

        ivAvatar = (CircleImageView) findViewById(R.id.id_avatar);
        rvLoginMore = (RelativeLayout) findViewById(R.id.id_login_more);
        tvLoginNick = (TextView) findViewById(R.id.id_login_nick);
        tvLoginInvate = (TextView) findViewById(R.id.id_login_invate);
        tvLoginCopy = (TextView) findViewById(R.id.id_login_invat_copy);

        ivSet.setOnClickListener(clickListener);
        tvLogin.setOnClickListener(clickListener);
        tvLoginCopy.setOnClickListener(clickListener);
        ivAvatar.setOnClickListener(clickListener);
    }

    public void update(UserInfo info){
        if(info == null){
            return;
        }

        uInfo = info;
        if(StringUtils.isNotEmpty(uInfo.getNick_name())) {
            tvLoginNick.setText(uInfo.getNick_name());
        }

        if(StringUtils.isNotEmpty(uInfo.getAvatar_url())) {
            ImageLoader.getInstance().displayImage(uInfo.getAvatar_url(), ivAvatar,
                    ImageLoaderUtils.getDefaultoptions());
        }

        if(StringUtils.isNotEmpty(uInfo.getInvitation_code())) {
            tvLoginInvate.setText("邀请码："+uInfo.getInvitation_code());
        }
    }

    private  OnClickListener clickListener = new OnClickListener(){
        public void onClick(View v){
            int id = v.getId();
            switch (id){
                case R.id.id_login_invat_copy:
                    copy();
                    break;
                case R.id.id_login:
                    goLogin();
                    break;
                case R.id.id_sets:
                    if(UserPreferences.isLogin()) {
                        goSet();
                    }else{
                        if(nologinDlg == null){
                            nologinDlg = new BindAlipayConfirmDlg(getContext(), new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goLogin();
                                    nologinDlg.dismiss();
                                }
                            },getResources().getString(R.string.toast_not_login2),getResources().getString(R.string.toast_gologin));
                        }

                        nologinDlg.show();
                    }
                    break;
                case R.id.id_avatar:
                    break;
            }
        }
    };

    private void goLogin(){
        Intent in = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(in);
    }

    private void goSet(){
        Intent in = new Intent(getContext(), SettingActivity.class);
        in.putExtra(SettingActivity.BUNDLE_USERINFO,uInfo);
        getContext().startActivity(in);
    }

    private void copy() {
        if (AppUtils.getSDKVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(uInfo.getInvitation_code());
        } else {
            ClipboardManager clip = (ClipboardManager) getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setPrimaryClip(ClipData.newPlainText("item_copy", uInfo.getInvitation_code()));
        }
        ToastUtils.showToast(getContext().getApplicationContext(), "信息已复制");
    }
}
