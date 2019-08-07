package com.sina.shopguide.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.LoginResult;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.GetSmsRequest;
import com.sina.shopguide.net.request.MobileIsExistRequest;
import com.sina.shopguide.net.request.RegisterRequest;
import com.sina.shopguide.net.requestinterface.IGetSmsRequest;
import com.sina.shopguide.net.requestinterface.IMobileIsExistRequest;
import com.sina.shopguide.net.requestinterface.IRegisterRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppConst;
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

public class RegisterActivity extends BaseActivity {

    private TextView tvRegistr;
    private boolean isPassEditng = false;
    private boolean isMobileEditng = false;
    private boolean isInvate = false;
    private boolean isSms = false;

    private TextView tvProtcal;
    private ImageView ivProtcalSel;

    private LoginTextEditView ltevMobile;
    private LoginTextEditView ltevPass;

    private LoginTextEditExView lteevInvate;
    private LoginTextEditExView lteevSms;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitleById(R.string.str_register);

        ltevMobile = (LoginTextEditView) findViewById(R.id.id_reg_mobile);
        ltevMobile.setEditTextType(InputType.TYPE_CLASS_NUMBER);

        ltevPass = (LoginTextEditView) findViewById(R.id.id_reg_password);
        ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        lteevInvate = (LoginTextEditExView) findViewById(R.id.id_reg_invate);
        lteevSms = (LoginTextEditExView) findViewById(R.id.id_reg_sms);

        tvRegistr = (TextView) findViewById(R.id.id_go_register);
        tvProtcal = (TextView) findViewById(R.id.id_protcal);
        ivProtcalSel = (ImageView) findViewById(R.id.id_protcal_sel);

        tvRegistr.setEnabled(false);

        tvProtcal.setOnClickListener(clickListener);
        tvRegistr.setOnClickListener(clickListener);
        ivProtcalSel.setOnClickListener(clickListener);

        ltevMobile.setEditingCallback(callbackEditing);
        ltevPass.setEditingCallback(callbackEditing);
        lteevInvate.setEditingCallback(callbackEditing);
        lteevSms.setEditingCallback(callbackEditing);

        lteevInvate.setOpOnClickListner(opInvateClickListener);
        lteevSms.setOpOnClickListner(opSmsClickListener);
        ltevPass.setOpOnClickListner(opClickListener);
    }

    private IOnEditingCallback callbackEditing = new IOnEditingCallback() {
        @Override
        public void isOnEditing(View v, boolean isEdit) {
            int id = v.getId();
            switch (id) {
                case R.id.id_reg_mobile:
                    isMobileEditng = isEdit;
                    break;
                case R.id.id_reg_password:
                    isPassEditng = isEdit;
                    break;
                case R.id.id_reg_invate:
                    isInvate = isEdit;
                    break;
                case R.id.id_reg_sms:
                    isSms = isEdit;
                    break;
            }
            adjustLoginShow();
        }
    };

    private void adjustLoginShow() {
        if (ivProtcalSel.getTag().equals("1") && isMobileEditng && isPassEditng && isSms) {
            tvRegistr.setBackgroundResource(R.drawable.bg_text_bigcorner_red);
            tvRegistr.setTextColor(getResources().getColor(R.color.colorWhite));
            tvRegistr.setEnabled(true);
        } else {
            tvRegistr.setBackgroundResource(R.drawable.bg_text_bigcorner_enable);
            tvRegistr.setTextColor(getResources().getColor(R.color.colorLine));
            tvRegistr.setEnabled(false);
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
        if(!AppUtils.isMobileNum(phoneNum)){
            ToastUtils.showToast(this,getString(R.string.toast_mobile_illegle));
            return ;
        }
        Retrofit retrofit = RetrofitClient.getInstance();
        IMobileIsExistRequest request = retrofit.create(IMobileIsExistRequest.class);
        final MobileIsExistRequest req = new MobileIsExistRequest();
        req.setMobilenum(phoneNum);
        Call<CommonApiResult<Boolean>> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<CommonApiResult<Boolean>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<CommonApiResult<Boolean>> call, Response<CommonApiResult<Boolean>> response) {
                // 步骤7：处理返回的数据结果
                if (response.body().getCode() == ErrorCode.SUCCESS.getCode()) {
                    if (response.body().getData()) {
                        ToastUtils.showToast(RegisterActivity.this, getString(R.string.toast_already_registed));
                    } else {
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
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<CommonApiResult<Boolean>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
                ToastUtils.showToast(RegisterActivity.this,"网络出错，请检查网络");
            }
        });
    }

    public void getSms() {
        String phoneNum = ltevMobile.getEditTextInfo();

        Retrofit retrofit = RetrofitClient.getInstance();
        IGetSmsRequest request = retrofit.create(IGetSmsRequest.class);
        final GetSmsRequest req = new GetSmsRequest();
        req.setMobilenum(phoneNum);
        req.setVerifyType(GetSmsRequest.TYPE_REGISTER);
        Call<FairyApiResult> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode() != ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(RegisterActivity.this,
                            AppUtils.getErrorMsg(response.body(), getString(R.string.toast_sms_getfailed)));
                    return;
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
                ToastUtils.showToast(RegisterActivity.this,"网络出错，请检查网络");
            }
        });
    }

    public void register() {
        String password = ltevPass.getEditTextInfo();
        if(StringUtils.isEmpty(password)){
            ToastUtils.showToast(RegisterActivity.this, getString(R.string.toast_pass_illegle));
            return;
        }
        int passLen = password.length();
        if(passLen<6 || passLen>30){
            ToastUtils.showToast(RegisterActivity.this, getString(R.string.toast_pass_illegle));
            return;
        }

        String smsCode = lteevSms.getEditTextInfo();
        if(smsCode.length()!= AppConst.SMS_LEN){
            ToastUtils.showToast(RegisterActivity.this, getString(R.string.toast_sms_illegle));
            return;
        }

        String phoneNum = ltevMobile.getEditTextInfo();
        String invateCode = lteevInvate.getEditTextInfo();

        Retrofit retrofit = RetrofitClient.getInstance();
        IRegisterRequest request = retrofit.create(IRegisterRequest.class);
        final RegisterRequest req = new RegisterRequest();
        req.setMobilenum(phoneNum);
        if (StringUtils.isNotEmpty(invateCode)) {
            req.setInvitationCode(invateCode);
        }
        req.setVerifyCode(smsCode);
        req.setPassword(Md5Utils.hexdigest(password));

        Call<CommonApiResult<LoginResult>> call = request.getCall(req.getParamsMapMd5Env());

        call.enqueue(new Callback<CommonApiResult<LoginResult>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<CommonApiResult<LoginResult>> call, Response<CommonApiResult<LoginResult>> response) {

                if (response.body().getCode() != ErrorCode.SUCCESS.getCode()) {
                    ToastUtils.showToast(RegisterActivity.this, response.body().getMsg());
                    return;
                }

                LoginResult ret = response.body().getData();
                UserPreferences.setLoginUser(ret.getId(), ret.getToken(), ret.getNick_name(), ret.getAvatar_url());
                UserPreferences.setLogin(true);
                ActivityUtils.gotoNextActivity(RegisterActivity.this, MainActivity.class);
                finish();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<CommonApiResult<LoginResult>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
                ToastUtils.showToast(RegisterActivity.this,"网络出错，请检查网络");
            }
        });
    }

    private View.OnClickListener opClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ltevPass.getEditTextType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT);
                ltevPass.changeRightDrawable(R.drawable.ic_show_password);
            } else {
                ltevPass.setEditTextType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ltevPass.changeRightDrawable(R.drawable.ic_hide_password);
            }
        }
    };

    private View.OnClickListener opInvateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ClipboardManager mClipboardManager;
            //剪切板Data对象
            ClipData mClipData;
            mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            mClipData = mClipboardManager.getPrimaryClip();
            //获取到内容
            ClipData.Item item = mClipData.getItemAt(0);
            String text = item.getText().toString();
            lteevInvate.setTip(text);
        }
    };

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.id_go_register:
                    register();
                    break;
                case R.id.id_protcal_sel:
                    if (view.getTag().equals("0")) {
                        ivProtcalSel.setImageResource(R.drawable.ic_aggrement_sel);
                        view.setTag("1");
                    } else {
                        ivProtcalSel.setImageResource(R.drawable.ic_aggrement_nosel);
                        view.setTag("0");
                    }
                    adjustLoginShow();
                    break;
                case R.id.id_protcal:
                    ActivityUtils.goWebActivity(RegisterActivity.this,AppConst.APP_PROTOCAL_URL);
                    break;
            }
        }
    };
}
