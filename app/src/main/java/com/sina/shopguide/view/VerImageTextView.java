package com.sina.shopguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class VerImageTextView extends RelativeLayout {
    private String strContent;
    private Drawable drawable;
    private Drawable drawableEnable;
    private ImageView ivImage;
    private TextView tvContent;

    private int contentSize=15;
    private int contentColor;
    public VerImageTextView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public VerImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerImageTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_ver_imagetext, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        if (ta != null) {
            drawable = ta.getDrawable(R.styleable.ImageTextView_iimage);
            drawableEnable = ta.getDrawable(R.styleable.ImageTextView_iimage_enable);
            strContent = ta.getString(R.styleable.ImageTextView_icontent);
            contentSize = ta.getDimensionPixelSize(R.styleable.ImageTextView_icontentsize, -1);
            contentColor = ta.getColor(R.styleable.ImageTextView_icontentcolor,-1);
            ta.recycle();
        }
        ivImage =(ImageView)findViewById(R.id.id_tag_icon);
        tvContent =(TextView)findViewById(R.id.id_tag_text);

        if(drawable != null){
            ivImage.setImageDrawable(drawable);
        }

        if(StringUtils.isNotEmpty(strContent)){
            tvContent.setText(strContent);
        }

        if(contentSize!=-1){
            tvContent.setTextSize(contentSize);
        }

        if(contentColor!=-1){
            tvContent.setTextColor(contentColor);
        }
    }

    public void setLocalEnable(boolean enable){
        if(!enable){
            ivImage.setImageDrawable(drawable);
        }else{
            ivImage.setImageDrawable(drawableEnable);
        }

    }
}
