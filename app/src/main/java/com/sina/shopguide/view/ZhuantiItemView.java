package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
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
import com.sina.shopguide.activity.ZhuantiDetailActivity;
import com.sina.shopguide.dto.zhuanti;
import com.sina.shopguide.util.ImageLoaderUtils;

import org.apache.commons.lang3.StringUtils;

public class ZhuantiItemView extends FrameLayout {

    private ImageView ivPic;

    private TextView tvTitle;

    private TextView tvDesc;

    private TextView tvView;

    private zhuanti product;

    public ZhuantiItemView(@NonNull Context context) {
        super(context);
        init();
    }

    public ZhuantiItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZhuantiItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ZhuantiItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_zhuanti_item, this);

        ivPic = (ImageView) findViewById(R.id.iv_pic);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvView = (TextView) findViewById(R.id.tv_view);
        this.setOnClickListener(clickListener);
    }

    public void setVVisible(){
        tvView.setVisibility(View.VISIBLE);
    }

    public OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent in = new Intent(getContext(), ZhuantiDetailActivity.class);
            in.putExtra(ZhuantiDetailActivity.PID,product.getId());
            getContext().startActivity(in);

        }
    };

    public void update(zhuanti p) {
        if(p == null ) {
            return;
        }

        if(product != p) {
            ivPic.setImageDrawable(null);
        }

        this.product = p;

        if(p.getBanner_url() != null && !p.getBanner_url().isEmpty()) {
            ImageLoader.getInstance().displayImage(p.getBanner_url(), ivPic, ImageLoaderUtils.getDefaultoptions());
        }
        if(StringUtils.isNotEmpty(p.getTitle())) {
            tvTitle.setText(p.getTitle());
        }

        if(StringUtils.isNotEmpty(p.getDescription())) {
            tvDesc.setText(p.getDescription());
        }

        tvView.setText(" "+p.getView());
    }
}
