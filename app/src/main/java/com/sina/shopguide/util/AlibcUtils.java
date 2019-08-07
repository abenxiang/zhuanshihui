package com.sina.shopguide.util;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcResultType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XiangWei on 18/6/13.
 */

public class AlibcUtils {

    private static final String DAOGOU_ISV_CODE = "daogou";

    private static boolean initSuccess;

    public static void initAlibcSdk(Application app) {
        AlibcTradeSDK.asyncInit(app, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                Log.i("daogou-bc", "baichuan init success");
                initSuccess = true;
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i("daogou-bc", "baichuan init fail:" + msg);
                initSuccess = false;
            }
        });
    }

    public static void openTaobaoUrl(final Activity context, final String taobaoUrl) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, DAOGOU_ISV_CODE);

        //实例化URL打开page
        AlibcBasePage page = new AlibcPage(taobaoUrl);

        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);

        //使用百川sdk提供默认的Activity打开detail
        AlibcTrade.show(context, page, showParams, null, exParams ,
                new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ActivityUtils.goWebActivity(context, taobaoUrl);
                    }
                });
    }

    /**
     * 打开淘宝订单列表
     * @param context
     */
    public static void showMyOrdersPage(Activity context){
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, DAOGOU_ISV_CODE);

        AlibcBasePage ordersPage = new AlibcMyOrdersPage(0, false);

        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);

        //使用百川sdk提供默认的Activity打开detail
        AlibcTrade.show(context, ordersPage, showParams, null, exParams ,
                new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                    }
                });
    }

    /**
     * 打开订单详情
     */
    public static void goTaobaoDetail(final Activity context, String pid){
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, DAOGOU_ISV_CODE);

        AlibcBasePage alibcBasePage;
        alibcBasePage = new AlibcDetailPage(pid);

        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);

        AlibcTrade.show(context, alibcBasePage, showParams, null, exParams,
                new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        //当addCartPage加购成功和其他page支付成功的时候会回调

                        if(tradeResult.resultType.equals(AlibcResultType.TYPECART)){
                            //加购成功
                            Toast.makeText(context.getApplicationContext(), "加购成功", Toast.LENGTH_SHORT).show();
                        }else if (tradeResult.resultType.equals(AlibcResultType.TYPEPAY)){
                            //支付成功
                            Toast.makeText(context.getApplicationContext(), "支付成功,成功订单号为"+tradeResult.payResult.paySuccessOrders, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int errCode, String errMsg) {
                        Toast.makeText(context.getApplicationContext(), "电商SDK出错,错误码="+errCode+" / 错误消息="+errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
