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

public class TopPanelTipView extends RelativeLayout {
    private String strContent;
    private String strTip;
    private boolean isHideArrow = false;
    private boolean isHideTip = false;
    private TextView tvTip;
    private TextView tvContent;
    private ImageView tvArrow;
    public TopPanelTipView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TopPanelTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopPanelTipView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_toppaneltip, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.TopPanelView);
        if (ta != null) {
            strTip = ta.getString(R.styleable.TopPanelView_tip);
            strContent = ta.getString(R.styleable.TopPanelView_content);
            isHideArrow = ta.getBoolean(R.styleable.TopPanelView_isHideArrow,false);
            isHideTip = ta.getBoolean(R.styleable.TopPanelView_isHideTip,false);
            ta.recycle();
        }
        tvTip =(TextView)findViewById(R.id.id_tag_tip);
        tvContent =(TextView)findViewById(R.id.id_tag_text);
        tvArrow =(ImageView)findViewById(R.id.id_tag_arrow);

        if(StringUtils.isNotEmpty(strTip)){
            tvTip.setText(strTip);
        }

        if(StringUtils.isNotEmpty(strContent)){
            tvContent.setText(strContent);
        }

        setArrawVisible(isHideArrow);
        setTipVisible(isHideTip);
    }

    public void setArrawVisible(boolean hide){
        if(!hide){
            tvArrow.setVisibility(View.VISIBLE);
        }else{
            tvArrow.setVisibility(View.INVISIBLE);
        }
    }

    public void setTipVisible(boolean hide){
        if(!hide){
            tvTip.setVisibility(View.VISIBLE);
        }else{
            tvTip.setVisibility(View.INVISIBLE);
        }
    }

    public void setTip(String tip){
        tvTip.setText(tip);
    }

    public void setTip(int resid){
        tvTip.setText(resid);
    }

    public String getTip(){
        return tvTip.getText().toString();
    }
}
