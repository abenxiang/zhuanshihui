package com.sina.shopguide.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.LoginResult;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.LoginRequest;
import com.sina.shopguide.net.requestinterface.ILoginRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.Md5Utils;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.IOnEditingCallback;
import com.sina.shopguide.view.LoginTextEditView;
//import com.sina.weibo.sdk.utils.MD5;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class LoginActivity extends BaseActivity {

    private TextView tvRegistr;
    private boolean isPassEditng = false;
    private boolean isMobileEditng = false;

    private TextView tvLogin;
    private TextView tvForgetPass;

    private LoginTextEditView ltevMobile;
    private LoginTextEditView ltevPass;

    private ImageView ivClose;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ltevMobile = (LoginTextEditView) findViewById(R.id.id_login_mobile);
        ltevMobile.setEditTextType(InputType.TYPE_CLASS_NUMBER);
        ltevPass = (LoginTextEditView) findViewById(R.id.id_login_password);
        ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ivClose = (ImageView) findViewById(R.id.id_login_close);

        tvLogin = (TextView) findViewById(R.id.id_login);
        tvRegistr = (TextView) findViewById(R.id.id_regiter);
        tvForgetPass = (TextView) findViewById(R.id.id_forgetpass);
        tvLogin.setEnabled(false);

        tvLogin.setOnClickListener(clickListener);
        tvRegistr.setOnClickListener(clickListener);
        tvForgetPass.setOnClickListener(clickListener);
        ivClose.setOnClickListener(clickListener);

        ltevMobile.setEditingCallback(callbackEditing);
        ltevPass.setEditingCallback(callbackEditing);
        ltevPass.setOpOnClickListner(opClickListener);
    }

    private View.OnClickListener opClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(ltevPass.getEditTextType() ==  (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT);
                ltevPass.changeRightDrawable(R.drawable.ic_show_password);
             } else {
                ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ltevPass.changeRightDrawable(R.drawable.ic_hide_password);
            }
        }
    };

    private IOnEditingCallback callbackEditing = new IOnEditingCallback(){
        @Override
        public void isOnEditing(View v,boolean isEdit) {
            int id = v.getId();
            switch (id) {
                case R.id.id_login_mobile:
                    isMobileEditng = isEdit;
                    break;
                case R.id. id_login_password:
                    isPassEditng = isEdit;
                    break;
            }
            adjustLoginShow();
        }
    };

    private void adjustLoginShow(){
        if(isMobileEditng && isPassEditng){
            tvLogin.setBackgroundResource(R.drawable.bg_text_bigcorner_red);
            tvLogin.setTextColor(getResources().getColor(R.color.colorWhite));
            tvLogin.setEnabled(true);
        }else{
            tvLogin.setBackgroundResource(R.drawable.bg_text_bigcorner_enable);
            tvLogin.setTextColor(getResources().getColor(R.color.colorLine));
            tvLogin.setEnabled(false);
        }
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id){
                case R.id.id_regiter:
                    ActivityUtils.gotoNextActivity(LoginActivity.this, RegisterActivity.class);
                    break;
                case R.id.id_login:
                    login();
                    break;
                case R.id.id_forgetpass:
                    ActivityUtils.gotoNextActivity(LoginActivity.this, ResetPasswordActivity.class,1);
                    break;
                case R.id.id_login_close:
                    finish();
                    break;
            }
        }
    };
    
    public void login(){
        String phoneNum = ltevMobile.getEditTextInfo();
        if(!AppUtils.isMobileNum(phoneNum)){
            ToastUtils.showToast(this,getString(R.string.toast_mobile_illegle));
            return ;
        }

        String password = ltevPass.getEditTextInfo();
        if(StringUtils.isEmpty(password)){
            ToastUtils.showToast(LoginActivity.this, getString(R.string.toast_pass_illegle));
            return;
        }
        int passLen = password.length();
        if(passLen<6 || passLen>30){
            ToastUtils.showToast(LoginActivity.this, getString(R.string.toast_pass_illegle));
            return;
        }

        Retrofit retrofit = RetrofitClient.getInstance();
        ILoginRequest request = retrofit.create(ILoginRequest.class);
        final LoginRequest req = new LoginRequest();
        req.setMobilenum(phoneNum);
        req.setPassword(Md5Utils.hexdigest(password));//password);//
        Call<CommonApiResult<LoginResult>> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<CommonApiResult<LoginResult>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<CommonApiResult<LoginResult>> call, Response<CommonApiResult<LoginResult>> response) {

                if(response.body().getCode()!= ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(LoginActivity.this,response.body().getMsg());
                    return;
                }

                LoginResult ret = response.body().getData();

                UserPreferences.setLoginUser(ret.getId(), ret.getToken(), ret.getNick_name(), ret.getAvatar_url());
                UserPreferences.setLogin(true);
//                ActivityUtils.gotoNextActivity(LoginActivity.this, MainActivity.class);

                setResult(RESULT_OK);
                finish();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<CommonApiResult<LoginResult>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
                ToastUtils.showToast(LoginActivity.this,"网络出错，请检查网络");
            }
        });
    }
}
