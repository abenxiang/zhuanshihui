package com.sina.shopguide.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.util.AppConst;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.MobShareUtils;
import com.sina.shopguide.util.ToastUtils;

import org.apache.commons.lang3.StringUtils;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by tiger on 18/5/8.
 */

public class InvateParnerActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvInvate;
    private TextView tvCopy;
    private UserInfo uInfo;
    private String title;
    private String desc;
    private String imgUrl="http://wm-merchant.oss-cn-qingdao.aliyuncs.com/daogou/4/3/1530175827_2661.jpg";;
    private String linkUrl;;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate_parner);
        uInfo = (UserInfo) getIntent().getSerializableExtra(SettingActivity.BUNDLE_USERINFO);
        tvInvate = (TextView) findViewById(R.id.id_invate);
        tvCopy = (TextView) findViewById(R.id.id_invate_copy);
        setTitleById(R.string.parner_title);

        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy();
            }
        });

        if (StringUtils.isNotEmpty(uInfo.getInvitation_code())) {
            tvInvate.setText("邀请码：" + uInfo.getInvitation_code());
        }
        linkUrl = AppConst.APP_DOWNLOAD_URL+ uInfo.getInvitation_code();
        desc = "大家快来一起用赚实惠吧，这个app上可以赚钱咯，我的邀请码是" + uInfo.getInvitation_code() + "，下载地址" + linkUrl;
        title = getString(R.string.parner_title);

        findViewById(R.id.tv_weibo).setOnClickListener(this);
        findViewById(R.id.tv_weixin).setOnClickListener(this);
        findViewById(R.id.tv_moments).setOnClickListener(this);
        findViewById(R.id.tv_qq).setOnClickListener(this);
        findViewById(R.id.tv_qq_zone).setOnClickListener(this);
    }

    private void copy() {
        if (AppUtils.getSDKVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(uInfo.getInvitation_code());
        } else {
            ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setPrimaryClip(ClipData.newPlainText("item_copy", uInfo.getInvitation_code()));
        }
        ToastUtils.showToast(getApplicationContext(), "信息已复制");
    }

    @Override
    public void onClick(View v) {
        String platForm = null;
        switch (v.getId()) {
            case R.id.tv_weibo:
                platForm = SinaWeibo.NAME;
                break;

            case R.id.tv_weixin:
                platForm = Wechat.NAME;
                break;

            case R.id.tv_moments:
                platForm = WechatMoments.NAME;
                break;

            case R.id.tv_qq:
                platForm =  QQ.NAME;
                break;

            case R.id.tv_qq_zone:
                platForm = QZone.NAME;
                break;

            default:
                break;
        }

        if(platForm != null){
            MobShareUtils.onekeyShare(InvateParnerActivity.this, imgUrl, title, desc, linkUrl, platForm);
        }
    }
}
