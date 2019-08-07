package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.sina.shopguide.activity.WebActivity;
import com.sina.shopguide.activity.WebNoTitleActivity;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.dto.SimpleProduct;
import com.sina.shopguide.util.ImageLoaderUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ProductSimpleItemView extends FrameLayout {

    private ImageView ivPic;

    private TextView tvTitle;

    private TextView tvPrice;

    private TextView tvCoupon;

    private ImageView tvCommision;

    private SimpleProduct product;

    public ProductSimpleItemView(@NonNull Context context) {
        super(context);

        init();
    }

    public ProductSimpleItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProductSimpleItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public ProductSimpleItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_simple_product_item, this);

        ivPic = (ImageView) findViewById(R.id.iv_pic);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvCoupon = (TextView) findViewById(R.id.tv_coupon);
        tvCommision = (ImageView) findViewById(R.id.tv_commision);
        tvCoupon.setPaintFlags(tvCoupon.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProductDetailActivity.class);
                Product p = new Product();
                p.setId(NumberUtils.toLong(product.getId(), 0));
                i.putExtra(ProductDetailActivity.EXTRA_PRODUCT, p);
                getContext().startActivity(i);
            }
        });
    }

    public void update(SimpleProduct p) {
        if(p == null ) {
            return;
        }

        if(product != p) {
            ivPic.setImageDrawable(null);
        }

        this.product = p;

        if(p.getPic() != null && !p.getPic().get(0).isEmpty()) {
            ImageLoader.getInstance().displayImage(p.getPic().get(0), ivPic, ImageLoaderUtils.getDefaultoptions());
        }
        if(StringUtils.isNotEmpty(p.getTitle())) {
            tvTitle.setText(p.getTitle());
        }

        tvPrice.setText(p.getCurrentPrice());
        tvCoupon.setText("¥"+p.getOrginPrice());

    }
}
