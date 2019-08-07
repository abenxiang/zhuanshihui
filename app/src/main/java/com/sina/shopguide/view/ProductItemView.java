package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.ProductDetailActivity;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.util.ImageLoaderUtils;
import com.sina.shopguide.util.UserPreferences;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by XiangWei on 18/5/11.
 */

public class ProductItemView extends FrameLayout implements View.OnClickListener {

    private ImageView ivPic;

    private TextView tvTitle;

    private TextView tvTaobaoPrice;

    private TextView tvSales;

    private TextView tvPrice;

    private TextView tvCoupon;

    private TextView tvCommision;

    private Product product;

    public ProductItemView(@NonNull Context context) {
        super(context);

        init();
    }

    public ProductItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProductItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public ProductItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_product_item, this);

        ivPic = (ImageView) findViewById(R.id.iv_pic);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTaobaoPrice = (TextView) findViewById(R.id.tv_taobao_price);
        tvSales = (TextView) findViewById(R.id.tv_sales_p_month);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvCoupon = (TextView) findViewById(R.id.tv_coupon);
        tvCommision = (TextView) findViewById(R.id.tv_commision);

        setOnClickListener(this);
    }

    public void update(Product p) {
        if(p == null ) {
            return;
        }

        if(product != p) {
            ivPic.setImageDrawable(null);
        }

        this.product = p;

        if(p.getPic() != null && !p.getPic().isEmpty()) {
            ImageLoader.getInstance().displayImage(p.getPic().get(0), ivPic, ImageLoaderUtils.getDefaultoptions());
        }
        if(StringUtils.isNotEmpty(p.getTitle())) {
            tvTitle.setText(p.getTitle());
        }

        final Resources res = getResources();
        tvTaobaoPrice.setText(res.getText(R.string.taobao_price) + " ¥" + p.getOrginPrice());
        tvSales.setText(res.getText(R.string.sales_p_month) + " " + p.getSales());
        tvPrice.setText("¥" + p.getCurrentPrice());
        tvCoupon.setText(res.getText(R.string.coupon) + " ¥" + p.getCouponPrice());
        if(!UserPreferences.isLogin() || StringUtils.isEmpty(p.getCommision())) {
            tvCommision.setVisibility(View.GONE);
        }
        else {
            tvCommision.setVisibility(View.VISIBLE);
            tvCommision.setText(res.getString(R.string.pre_commision) + p.getCommision());
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getContext(), ProductDetailActivity.class);
        i.putExtra(ProductDetailActivity.EXTRA_PRODUCT, product);
        getContext().startActivity(i);
    }
}
