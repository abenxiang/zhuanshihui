package com.sina.shopguide.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sina.shopguide.R;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by XiangWei on 18/5/22.
 */

public final class MobShareUtils {

    public static void showOneKeyShare(Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(context);
    }

    public static void shareToWeibo(Context context, String title, String imageUrl, String url) {
        Platform.ShareParams params = new Platform.ShareParams();

        params.setText(title + " " + url);
        if (StringUtils.isNotEmpty(imageUrl)) {
            params.setImageUrl(imageUrl);
        }

        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.share(params);
    }

    public static void shareToWeixin(Context context, String title, String url, String imageUrl) {
        if (StringUtils.isEmpty(url)) {
            return;
        }

        Platform.ShareParams params = new Platform.ShareParams();

        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setUrl(url);

        params.setTitle(title);
        params.setText(title);
        if(StringUtils.isNotEmpty(imageUrl)) {
            params.setImageUrl(imageUrl);
        }

        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        weixin.share(params);
    }

    public static void shareToMoments(Context context, String title, String url, String imageUrl) {
        if (StringUtils.isEmpty(url)) {
            return;
        }

        Platform.ShareParams params = new Platform.ShareParams();

        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setUrl(url);

        params.setTitle(title);
        params.setText(title);
        if(StringUtils.isNotEmpty(imageUrl)) {
            params.setImageUrl(imageUrl);
        }

        Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
        weixin.share(params);
    }

    public static void shareToQQ(Context context, String title, String desc,
                                 String imagePath, String url) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);// 标题的超链接
        sp.setText(desc);
        sp.setImageUrl(imagePath);
        sp.setSite("导购");
        sp.setSiteUrl("http://daogou.weimai.com");

        Platform qq = ShareSDK.getPlatform(QQ.NAME);

        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qq.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
            }

            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
            }
        });
        qq.share(sp);
    }

    public static void shareToQQZone(Context context, String title, String desc,
                                     String imagePath, String url) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);// 标题的超链接
        sp.setText(desc);
        sp.setImageUrl(imagePath);
        sp.setSite("导购");
        sp.setSiteUrl("http://daogou.weimai.com");

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);

        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
            }

            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
            }
        });
        // 执行图文分享
        qzone.share(sp);
    }

    public static void onekeyShare(final Context context, String img, String title,
                          String content, String linkurl,String platform){

        final OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);

        oks.setTitleUrl(linkurl);

        oks.setText(content);
        // 网络图片
        oks.setImageUrl(img);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(linkurl);

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(linkurl);// CustomShareFieldsPage.getString("siteUrl",
        // "http://mob.com"));

        // 是否直接分享（true则直接分享）
        oks.setSilent(false);
        //oks.setShareFromQQAuthSupport(false);
        oks.setTheme(OnekeyShareTheme.CLASSIC);

        // 令编辑页面显示为Dialog模式
        oks.setDialogMode(true);
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();

        if(StringUtils.isNotEmpty(platform)) {
            oks.setPlatform(platform);
        }
        oks.show(context);
    }
}
