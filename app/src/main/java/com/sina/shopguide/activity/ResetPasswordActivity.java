package com.sina.shopguide.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.GetSmsRequest;
import com.sina.shopguide.net.request.ResetPassRequest;
import com.sina.shopguide.net.requestinterface.IGetSmsRequest;
import com.sina.shopguide.net.requestinterface.IResetPassRequest;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.Md5Utils;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.IOnEditingCallback;
import com.sina.shopguide.view.LoginTextEditExView;
import com.sina.shopguide.view.LoginTextEditView;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class ResetPasswordActivity extends BaseActivity {

    private TextView tvCommit;
    private boolean isPassEditng = false;
    private boolean isMobileEditng = false;
    private boolean isSms = false;

    private LoginTextEditView ltevMobile;
    private LoginTextEditView ltevPass;

    private LoginTextEditExView lteevSms;

    private int extra=0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        setTitleById(R.string.str_resetpass);
        extra = getIntent().getIntExtra("extra",0);
        ltevMobile =(LoginTextEditView) findViewById(R.id.id_reset_mobile);
        ltevPass =(LoginTextEditView) findViewById(R.id.id_reset_password);
        ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        lteevSms =(LoginTextEditExView) findViewById(R.id.id_reset_sms);

        tvCommit =(TextView) findViewById(R.id.id_go_commit);

        tvCommit.setEnabled(false);

        tvCommit.setOnClickListener(clickListener);

        ltevMobile.setEditingCallback(callbackEditing);
        ltevPass.setEditingCallback(callbackEditing);
        lteevSms.setEditingCallback(callbackEditing);

        lteevSms.setOpOnClickListner(opSmsClickListener);
        ltevPass.setOpOnClickListner(opClickListener);
    }

    private IOnEditingCallback callbackEditing = new IOnEditingCallback(){
        @Override
        public void isOnEditing(View v, boolean isEdit) {
            int id = v.getId();
            switch (id) {
                case R.id.id_reset_mobile:
                    isMobileEditng = isEdit;
                    break;
                case R.id.id_reset_password:
                    isPassEditng = isEdit;
                    break;
                case R.id.id_reset_sms:
                    isSms = isEdit;
                    break;
            }
            adjustLoginShow();
        }
    };

    private void adjustLoginShow(){
        if(isMobileEditng && isPassEditng && isSms){
            tvCommit.setBackgroundResource(R.drawable.bg_text_bigcorner_red);
            tvCommit.setTextColor(getResources().getColor(R.color.colorWhite));
            tvCommit.setEnabled(true);
        }else{
            tvCommit.setBackgroundResource(R.drawable.bg_text_bigcorner_enable);
            tvCommit.setTextColor(getResources().getColor(R.color.colorLine));
            tvCommit.setEnabled(false);
        }
    }

    private View.OnClickListener opSmsClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phoneNum = ltevMobile.getEditTextInfo();
            if(!AppUtils.isMobileNum(phoneNum)){
                ToastUtils.showToast(ResetPasswordActivity.this,getString(R.string.toast_mobile_illegle));
                return ;
            }
            getSms();
            //lteevSms.set
            final TextView tv = (TextView) view;

            CountDownTimer timer = new CountDownTimer(61 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv.setBackgroundResource(R.drawable.bg_text_stoke_gray_corner);
                    tv.setTextColor(getResources().getColor(R.color.colorTopPanelTip));
                    int left = (int) (millisUntilFinished / 1000);
                    tv.setText(left + "秒");//后重新发送
                }

                @Override
                public void onFinish() {
                    tv.setBackgroundResource(R.drawable.bg_text_stoke_red_corner);
                    tv.setTextColor(getResources().getColor(R.color.colorRedTitle));
                    tv.setEnabled(true);
                    tv.setText(getString(R.string.bindalipay_smscode_get));
                }
            };

            timer.start();
        }
    };

    public void getSms() {
        String phoneNum = ltevMobile.getEditTextInfo();
        Retrofit retrofit = RetrofitClient.getInstance();
        IGetSmsRequest request = retrofit.create(IGetSmsRequest.class);

        final GetSmsRequest req = new GetSmsRequest();
        req.setMobilenum(phoneNum);
        req.setVerifyType(GetSmsRequest.TYPE_RESET);
        Call<FairyApiResult> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                // 步骤7：处理返回的数据结果
                if(response.body().getCode() != ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(ResetPasswordActivity.this,
                            AppUtils.getErrorMsg(response.body(), getString(R.string.toast_sms_getfailed)));
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id){
                case R.id.id_go_commit:
                    commit();
                    break;
            }
        }
    };

    private void commit(){
        String phoneNum = ltevMobile.getEditTextInfo();
        String smsCode = lteevSms.getEditTextInfo();
        String password = ltevPass.getEditTextInfo();
        if(StringUtils.isEmpty(password) || password.length() < 6|| password.length()>30){
            ToastUtils.showToast(ResetPasswordActivity.this,"密码设置不合理");
            return ;
        }

        Retrofit retrofit = RetrofitClient.getInstance();
        IResetPassRequest request = retrofit.create(IResetPassRequest.class);
        final ResetPassRequest req = new ResetPassRequest();
        req.setMobilenum(phoneNum);
        req.setVerifyCode(smsCode);
        req.setPassword(Md5Utils.hexdigest(password));


        Call<FairyApiResult> call = UserPreferences.isLogin()?request.getCall(req.getParamsMap()):request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if (response.body().getCode() != ErrorCode.SUCCESS.getCode()) {
                    ToastUtils.showToast(ResetPasswordActivity.this, response.body().getMsg());
                    return;
                }
                UserPreferences.removeUserInfo();
                if(extra == 0)
                    ActivityUtils.gotoNextActivity(ResetPasswordActivity.this, LoginActivity.class);
                finish();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }
}
