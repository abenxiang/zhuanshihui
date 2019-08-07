package com.sina.shopguide.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ModifyUserRequest;
import com.sina.shopguide.net.requestinterface.IModifyUserRequest;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.ToastUtils;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class BindInvateCodeActivity extends BaseActivity {
    private TextView tvGobind;
    private EditText etCode;

    private String strCode;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        strCode = getIntent().getStringExtra(TextEditSingleActivity.BUDLE_EXTA_CONTENT);
        setContentView(R.layout.activity_bind_invatecode);
        tvGobind  = (TextView)findViewById(R.id.id_gobind);
        etCode  = (EditText)findViewById(R.id.id_invate_code);
        setTitle(getString(R.string.title_become_parner));

        tvGobind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindInvateCode();
            }
        });
    }

    private void bindInvateCode(){
        String num = etCode.getText().toString();
        if(StringUtils.isEmpty(num)) {
            ToastUtils.showToast(BindInvateCodeActivity.this,getString(R.string.toast_param_invalide));
            return;
        }

        if(StringUtils.isNotEmpty(strCode)&& strCode.equals(num)){
            ToastUtils.showToast(BindInvateCodeActivity.this,"不能绑定自己的邀请码");
            return;
        }
        Retrofit retrofit = RetrofitClient.getInstance();
        IModifyUserRequest request = retrofit.create(IModifyUserRequest.class);
        final ModifyUserRequest req = new ModifyUserRequest();
        req.setInvitationCode(num);

        Call<FairyApiResult> call = request.getCall(req.getParamsMap());
        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode()==ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(BindInvateCodeActivity.this,getString(R.string.toast_bindinvate_sucess));
                    finish();
                    return;
                }else {
                    ToastUtils.showToast(BindInvateCodeActivity.this,response.body().getMsg());
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                ToastUtils.showToast(BindInvateCodeActivity.this,getString(R.string.toast_bindinvate_fail));
            }
        });
    }
}
