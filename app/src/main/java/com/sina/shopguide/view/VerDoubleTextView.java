package com.sina.shopguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class VerDoubleTextView extends RelativeLayout {
    private String strContent;
    private String strTip;
    private TextView tvContent;
    private TextView tvTip;

    private int contentSize=15;
    private int tipSize=15;
    private int contentColor;
    private int tipColor;

    public VerDoubleTextView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public VerDoubleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerDoubleTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_ver_doubletext, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.DoubleTextView);
        if (ta != null) {
            strContent = ta.getString(R.styleable.DoubleTextView_dcontent);
            strTip = ta.getString(R.styleable.DoubleTextView_dtip);
            contentSize = ta.getDimensionPixelSize(R.styleable.DoubleTextView_dcontentsize, -1);
            contentColor = ta.getColor(R.styleable.DoubleTextView_dcontentcolor,-1);
            tipSize = ta.getDimensionPixelSize(R.styleable.DoubleTextView_dtipsize, -1);
            tipColor = ta.getColor(R.styleable.DoubleTextView_dtipcolor,-1);
            ta.recycle();
        }
        tvTip =(TextView)findViewById(R.id.id_tag_content);
        tvContent =(TextView)findViewById(R.id.id_tag_text);

        if(StringUtils.isNotEmpty(strContent)){
            tvContent.setText(strContent);
        }

        if(StringUtils.isNotEmpty(strTip)){
            tvTip.setText(strTip);
        }

        if(contentSize!=-1){
            tvContent.setTextSize(contentSize);
        }

        if(tipSize!=-1){
            tvTip.setTextSize(tipSize);
        }

        if(contentColor!=-1){
            tvContent.setTextColor(contentColor);
        }

        if(tipColor!=-1){
            tvTip.setTextColor(tipColor);
        }
    }

    public void setTip(String tips){
        tvTip.setText(tips);
    }

    public void setLocalEnable(boolean enable){
        if(enable){
            tvTip.setTextColor(getContext().getResources().getColor(R.color.colorRedTitle));
        }else{
            tvTip.setTextColor(getContext().getResources().getColor(R.color.colorTopPanelTip));
        }

    }
}
