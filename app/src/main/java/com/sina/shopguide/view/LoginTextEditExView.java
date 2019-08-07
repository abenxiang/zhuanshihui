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

public class LoginTextEditExView extends RelativeLayout {
    private String strContent;
    private String strTip;
    private String strOpr;

    private Drawable drawableSel;
    private Drawable drawableUnSel;

    private ImageView ivLeft;
    private TextView tvRight;

    private EditText etTip;

    public LoginTextEditExView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public LoginTextEditExView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoginTextEditExView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_login_textedit_ex, this);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.LoginEditTextView);
        if (ta != null) {
            strContent = ta.getString(R.styleable.LoginEditTextView_letv_content);
            strTip = ta.getString(R.styleable.LoginEditTextView_letv_tip);
            strOpr = ta.getString(R.styleable.LoginEditTextView_letv_opcontent);

            drawableSel = ta.getDrawable(R.styleable.LoginEditTextView_letv_drawable);
            drawableUnSel = ta.getDrawable(R.styleable.LoginEditTextView_letv_undrawable);
            ta.recycle();
        }

        etTip =(EditText)findViewById(R.id.id_tag_content);
        ivLeft =(ImageView)findViewById(R.id.id_tag_lefticon);
        tvRight =(TextView) findViewById(R.id.id_tag_righticon);

        if(StringUtils.isNotEmpty(strContent)){
            etTip.setText(strContent);
        }

        if(StringUtils.isNotEmpty(strTip)){
            etTip.setHint(strTip);
        }

        if(StringUtils.isNotEmpty(strOpr)) {
            tvRight.setText(strOpr);
        }

        if(drawableUnSel != null){
            ivLeft.setImageDrawable(drawableUnSel);
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
                    callbackEditing.isOnEditing(LoginTextEditExView.this, isSel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etTip.setText("");
            }
        });
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

    private OnClickListener opClickListener;

    public void setOpOnClickListner(OnClickListener clickListner){
        opClickListener = clickListner;
        tvRight.setOnClickListener(opClickListener);
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

    public String getEditTextInfo(){
        return etTip.getText().toString();
    }
}
