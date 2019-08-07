package com.sina.shopguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class TopPanelView extends RelativeLayout {
    private String strContent;
    private Drawable drawable;
    private boolean isHideArrow = false;
    private boolean isHideImage = false;
    private ImageView ivImage;
    private TextView tvContent;
    private ImageView tvArrow;
    public TopPanelView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TopPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopPanelView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_toppanel, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.TopPanelView);
        if (ta != null) {
            drawable = ta.getDrawable(R.styleable.TopPanelView_image);
            strContent = ta.getString(R.styleable.TopPanelView_content);
            isHideArrow = ta.getBoolean(R.styleable.TopPanelView_isHideImage,false);
            isHideImage = ta.getBoolean(R.styleable.TopPanelView_isHideArrow,false);
            ta.recycle();
        }
        ivImage =(ImageView)findViewById(R.id.id_tag_icon);
        tvContent =(TextView)findViewById(R.id.id_tag_text);
        tvArrow =(ImageView)findViewById(R.id.id_tag_arrow);

        if(drawable != null){
            ivImage.setImageDrawable(drawable);
        }

        if(StringUtils.isNotEmpty(strContent)){
            tvContent.setText(strContent);
        }

        setArrawVisible(isHideArrow);
        setImageVisible(isHideImage);
    }

    public void setArrawVisible(boolean hide){
        if(!hide){
            tvArrow.setVisibility(View.VISIBLE);
        }else{
            tvArrow.setVisibility(View.INVISIBLE);
        }
    }

    public void setImageVisible(boolean hide){
        if(!hide){
            ivImage.setVisibility(View.VISIBLE);
        }else{
            ivImage.setVisibility(View.INVISIBLE);
        }
    }
}
