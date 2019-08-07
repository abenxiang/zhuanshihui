package com.sina.shopguide.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.GetSmsRequest;
import com.sina.shopguide.net.request.MobileIsExistRequest;
import com.sina.shopguide.net.request.ModifyMobileRequest;
import com.sina.shopguide.net.requestinterface.IGetSmsRequest;
import com.sina.shopguide.net.requestinterface.IMobileIsExistRequest;
import com.sina.shopguide.net.requestinterface.IModifyMobileRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppConst;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.BindTextEditExView;
import com.sina.shopguide.view.BindTextEditView;
import com.sina.shopguide.view.IOnEditingCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class BindMobileActivity extends BaseActivity {

    private TextView tvBind;
    private boolean isMobileEditng = false;
    private boolean isSms = false;

    private BindTextEditView ltevMobile;
    private BindTextEditExView lteevSms;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        setTitleById(R.string.str_bind_mobile);

        ltevMobile = (BindTextEditView) findViewById(R.id.id_bind_mobile);
        ltevMobile.setEditTextType(InputType.TYPE_CLASS_NUMBER);

        lteevSms = (BindTextEditExView) findViewById(R.id.id_bind_sms);

        tvBind = (TextView) findViewById(R.id.id_go_bind);

        tvBind.setEnabled(false);

        tvBind.setOnClickListener(clickListener);

        ltevMobile.setEditingCallback(callbackEditing);
        lteevSms.setEditingCallback(callbackEditing);

        lteevSms.setOpOnClickListner(opSmsClickListener);
    }

    private IOnEditingCallback callbackEditing = new IOnEditingCallback() {
        @Override
        public void isOnEditing(View v, boolean isEdit) {
            int id = v.getId();
            switch (id) {
                case R.id.id_bind_mobile:
                    isMobileEditng = isEdit;
                    break;
                case R.id.id_bind_sms:
                    isSms = isEdit;
                    break;
            }
            adjustLoginShow();
        }
    };

    private void adjustLoginShow() {
        if (isMobileEditng && isSms) {
            tvBind.setBackgroundResource(R.drawable.bg_text_bigcorner_red);
            tvBind.setTextColor(getResources().getColor(R.color.colorWhite));
            tvBind.setEnabled(true);
        } else {
            tvBind.setBackgroundResource(R.drawable.bg_text_bigcorner_enable);
            tvBind.setTextColor(getResources().getColor(R.color.colorLine));
            tvBind.setEnabled(false);
        }
    }

    private View.OnClickListener opSmsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isMobileExist(view);
        }
    };

    public void isMobileExist(final View view) {
        String phoneNum = ltevMobile.getEditTextInfo();
        Retrofit retrofit = RetrofitClient.getInstance();
        IMobileIsExistRequest request = retrofit.create(IMobileIsExistRequest.class);
        final MobileIsExistRequest req = new MobileIsExistRequest();
        req.setMobilenum(phoneNum);
        Call<CommonApiResult<Boolean>> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<CommonApiResult<Boolean>>() {
            @Override
            public void onResponse(Call<CommonApiResult<Boolean>> call, Response<CommonApiResult<Boolean>> response) {

                if (response.body().getCode() == ErrorCode.SUCCESS.getCode()) {
                    if (response.body().getData()) {
                        ToastUtils.showToast(BindMobileActivity.this, getString(R.string.toast_already_registed));
                    } else {
                        getSms();
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
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<CommonApiResult<Boolean>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }

    public void getSms() {
        String phoneNum = ltevMobile.getEditTextInfo();
        if(!AppUtils.isMobileNum(phoneNum)){
            ToastUtils.showToast(this,"手机号码不合法");
            return ;
        }
        Retrofit retrofit = RetrofitClient.getInstance();
        IGetSmsRequest request = retrofit.create(IGetSmsRequest.class);
        final GetSmsRequest req = new GetSmsRequest();
        req.setMobilenum(phoneNum);
        req.setVerifyType(GetSmsRequest.TYPE_BIND_ALI);
        Call<FairyApiResult> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode()!=ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(BindMobileActivity.this,getString(R.string.toast_sms_getfailed));
                    return;
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }

    public void bind() {
        String phoneNum = ltevMobile.getEditTextInfo();
        String smsCode = lteevSms.getEditTextInfo();
        if(smsCode.length()!= AppConst.SMS_LEN){
            ToastUtils.showToast(BindMobileActivity.this, getString(R.string.toast_sms_illegle));
        }

        Retrofit retrofit = RetrofitClient.getInstance();
        IModifyMobileRequest request = retrofit.create(IModifyMobileRequest.class);
        final ModifyMobileRequest req = new ModifyMobileRequest();
        req.setMobilenum(phoneNum);
        req.setVerifyCode(smsCode);

        Call<FairyApiResult> call = request.getCall(req.getParamsMap());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {

                if (response.body().getCode() != ErrorCode.SUCCESS.getCode()) {
                    ToastUtils.showToast(BindMobileActivity.this, response.body().getMsg());
                    return;
                }

                UserPreferences.removeUserInfo();
                ActivityUtils.gotoNextActivity(BindMobileActivity.this, LoginActivity.class);
                finish();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }


    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.id_go_register:
                    bind();
                    break;
            }
        }
    };
}
