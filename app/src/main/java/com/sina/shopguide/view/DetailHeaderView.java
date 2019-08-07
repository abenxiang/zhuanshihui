package com.sina.shopguide.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.Product;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by XiangWei on 18/5/17.
 */

public class DetailHeaderView extends FrameLayout {

    private DetailBannerView bannerView;

    private TextView tvTitle;

    private TextView tvTaobaoPrice;

    private TextView tvSales;

    private TextView tvPrice;

    private TextView tvCoupon;

    private Product product;

    public DetailHeaderView(@NonNull Context context) {
        super(context);

        init();
    }

    public DetailHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public DetailHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public DetailHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_detail_header, this);

        bannerView = (DetailBannerView) findViewById(R.id.v_detail_banner);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTaobaoPrice = (TextView) findViewById(R.id.tv_taobao_price);
        tvSales = (TextView) findViewById(R.id.tv_sales_p_month);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvCoupon = (TextView) findViewById(R.id.tv_coupon);
    }

    public void update(Product p) {
        if(p == null) {
            return;
        }

        this.product = p;

        bannerView.update(p);

        if(StringUtils.isNotEmpty(p.getTitle())) {
            tvTitle.setText(p.getTitle());
        }

        final Resources res = getResources();
        tvTaobaoPrice.setText(res.getText(R.string.taobao_price) + " ¥" + p.getOrginPrice());
        tvSales.setText(res.getText(R.string.sales_p_month) + " " + p.getSales());
        tvPrice.setText(p.getCurrentPrice());
        tvCoupon.setText(res.getText(R.string.coupon) + " ¥" + p.getCouponPrice());
    }
}
