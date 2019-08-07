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

public class LoginTextEditView extends RelativeLayout {
    private String strContent;
    private String strTip;

    private Drawable drawableSel;
    private Drawable drawableUnSel;
    private Drawable drawableRight;

    private ImageView ivLeft;
    private ImageView ivRight;

    private EditText etTip;

    public LoginTextEditView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public LoginTextEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoginTextEditView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_login_textedit, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.LoginEditTextView);
        if (ta != null) {
            strContent = ta.getString(R.styleable.LoginEditTextView_letv_content);
            strTip = ta.getString(R.styleable.LoginEditTextView_letv_tip);

            drawableSel = ta.getDrawable(R.styleable.LoginEditTextView_letv_drawable);
            drawableUnSel = ta.getDrawable(R.styleable.LoginEditTextView_letv_undrawable);
            drawableRight = ta.getDrawable(R.styleable.LoginEditTextView_letv_rightdrawable);
            ta.recycle();
        }

        etTip =(EditText)findViewById(R.id.id_tag_content);
        ivLeft =(ImageView)findViewById(R.id.id_tag_lefticon);
        ivRight =(ImageView)findViewById(R.id.id_tag_righticon);

        if(StringUtils.isNotEmpty(strContent)){
            etTip.setText(strContent);
        }

        if(StringUtils.isNotEmpty(strTip)){
            etTip.setHint(strTip);
        }

        if(drawableUnSel != null){
            ivLeft.setImageDrawable(drawableUnSel);
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
                changeLeftDrawable(isSel);

                if(callbackEditing != null){
                    callbackEditing.isOnEditing(LoginTextEditView.this, isSel);
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

    public void changeLeftDrawable(boolean isel){
        if(isel){
            ivLeft.setImageDrawable(drawableSel);
        }else{
            ivLeft.setImageDrawable(drawableUnSel);
        }
    }

    public void changeRightDrawable(int resId){
        ivRight.setImageResource(resId);
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
