package com.sina.shopguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class BindTextEditView extends RelativeLayout {
    private String strContent;
    private String strTip;
    private Drawable drawableRight;

    private TextView tvLeft;
    private ImageView ivRight;

    private EditText etTip;

    public BindTextEditView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public BindTextEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BindTextEditView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_bind_textedit, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.LoginEditTextView);
        if (ta != null) {
            strContent = ta.getString(R.styleable.LoginEditTextView_letv_content);
            strTip = ta.getString(R.styleable.LoginEditTextView_letv_tip);
            drawableRight = ta.getDrawable(R.styleable.LoginEditTextView_letv_rightdrawable);
            ta.recycle();
        }

        etTip =(EditText)findViewById(R.id.id_tag_content);
        tvLeft =(TextView)findViewById(R.id.id_tag_lefticon);
        ivRight =(ImageView)findViewById(R.id.id_tag_righticon);

        if(StringUtils.isNotEmpty(strContent)){
            etTip.setText(strContent);
        }

        if(StringUtils.isNotEmpty(strTip)){
            etTip.setHint(strTip);
        }

        if(drawableRight != null){
            ivRight.setImageDrawable(drawableRight);
        }

        etTip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isSel = (charSequence.length()>0?true:false);
                //changeLeftDrawable(isSel);

                if(callbackEditing != null){
                    callbackEditing.isOnEditing(BindTextEditView.this, isSel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etTip.setText("");
            }
        });
    }

    private OnClickListener opClickListener;

    public void setOpOnClickListner(OnClickListener clickListner){
        opClickListener = clickListner;
        ivRight.setOnClickListener(opClickListener);
    }

    private IOnEditingCallback callbackEditing ;

    public void setEditingCallback(IOnEditingCallback callback){
        callbackEditing = callback;
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

    public void setEditTextType(int type){
        etTip.setInputType(type);
    }

    public int getEditTextType(){
        return etTip.getInputType();
    }

    public String getEditTextInfo(){
        return etTip.getText().toString();
    }
}
