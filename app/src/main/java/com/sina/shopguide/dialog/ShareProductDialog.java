package com.sina.shopguide.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.util.MobShareUtils;

/**
 * 分享商品ialog
 * Created by XiangWei on 18/5/24.
 */

public class ShareProductDialog extends Dialog implements View.OnClickListener {

    private TextView tvRulesDetail;

    private TextView tvExpandRules;

    private TextView tvRewardValue;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShareProductDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialogTheme);
    }

    public ShareProductDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public ShareProductDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dlg_share_product);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        tvRulesDetail = (TextView) findViewById(R.id.tv_rules_detail);
        tvExpandRules = (TextView) findViewById(R.id.tv_expand_rules);
        tvRewardValue = (TextView) findViewById(R.id.tv_reward_value);

        findViewById(R.id.tv_weibo).setOnClickListener(this);
        findViewById(R.id.tv_weixin).setOnClickListener(this);
        findViewById(R.id.tv_moments).setOnClickListener(this);
        findViewById(R.id.tv_qq).setOnClickListener(this);
        findViewById(R.id.tv_qq_zone).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_expand_rules).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_weibo:
                if(product != null && !product.getPic().isEmpty()) {
                    MobShareUtils.shareToWeibo(getContext(), product.getTitle(), product.getPic().get(0),
                            product.getLink());
                }
                dismiss();
                break;

            case R.id.tv_weixin:
                if(product != null && !product.getPic().isEmpty()) {
                    MobShareUtils.shareToWeixin(getContext(), product.getTitle(), product.getLink(),
                            product.getPic().get(0));
                }
                dismiss();
                break;

            case R.id.tv_moments:
                if(product != null && !product.getPic().isEmpty()) {
                    MobShareUtils.shareToMoments(getContext(), product.getTitle(), product.getLink(),
                            product.getPic().get(0));
                }
                dismiss();
                break;

            case R.id.tv_qq:
                if(product != null && !product.getPic().isEmpty()) {
                    MobShareUtils.shareToQQ(getContext(), product.getTitle(), product.getTitle(),
                            product.getPic().get(0), product.getLink());
                }
                dismiss();
                break;

            case R.id.tv_qq_zone:
                if(product != null && !product.getPic().isEmpty()) {
                    MobShareUtils.shareToQQZone(getContext(), product.getTitle(), product.getTitle(),
                            product.getPic().get(0), product.getLink());
                }
                dismiss();
                break;

            case R.id.tv_expand_rules:
                if(tvRulesDetail.getVisibility() == View.GONE) {
                    tvRulesDetail.setVisibility(View.VISIBLE);
                    tvExpandRules.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_expland_rules, 0);
                }
                else {
                    tvRulesDetail.setVisibility(View.GONE);
                    tvExpandRules.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_close_rules, 0);
                }
                break;

            case R.id.tv_cancel:
                dismiss();
                break;

            default:
                break;
        }

    }

    @Override
    public void show() {
        super.show();
        updateUI();
    }

    private void updateUI() {
        if(product == null) {
            return;
        }

        tvRewardValue.setText(product.getCommision());
    }
}
