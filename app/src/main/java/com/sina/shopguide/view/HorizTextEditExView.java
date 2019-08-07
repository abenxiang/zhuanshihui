package com.sina.shopguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class HorizTextEditExView extends RelativeLayout {
    private String strContent;
    private String strTip;
    private TextView tvContent;
    private EditText etTip;
    private TextView tvOpr;

    private int contentSize=15;
    private int tipSize=15;
    private int contentColor;
    private int tipColor;

    public HorizTextEditExView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public HorizTextEditExView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HorizTextEditExView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_horiz_textedit_ex, this);
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
        etTip =(EditText)findViewById(R.id.id_tag_content);
        tvContent =(TextView)findViewById(R.id.id_tag_text);
        tvOpr =(TextView)findViewById(R.id.id_tag_opr);

        if(StringUtils.isNotEmpty(strContent)){
            tvContent.setText(strContent);
        }

        if(StringUtils.isNotEmpty(strTip)){
            etTip.setHint(strTip);
        }

        if(contentSize!=-1){
            tvContent.setTextSize(contentSize);
        }

        if(tipSize!=-1){
            etTip.setTextSize(tipSize);
        }

        if(contentColor!=-1){
            tvContent.setTextColor(contentColor);
        }

        if(tipColor!=-1){
            etTip.setTextSize(tipColor);
        }

        tvOpr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setTip(String tips){
        etTip.setText(tips);
    }
    public void setTipHint(int resid){
        etTip.setHint(resid);
    }

    public String getTip(){
        return etTip.getText().toString();
    }
}
