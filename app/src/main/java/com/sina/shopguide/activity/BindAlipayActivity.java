package com.sina.shopguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dialog.BindAlipayConfirmDlg;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.BindAliRequest;
import com.sina.shopguide.net.requestinterface.IBindAliRequest;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.view.HorizTextEditView;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class BindAlipayActivity extends BaseActivity {
    public static final String ALI_NAME="aliname";

    private TextView tvGobind;
    private EditText etCode;
//  private HorizTextEditExView hteevSms;
    private HorizTextEditView htevName;
    private HorizTextEditView htevAliaccount;
//  private HorizTextEditView htevMobile;
    private BindAlipayConfirmDlg dlg;
    private String aliName;
    private String modStr;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        aliName = getIntent().getStringExtra(TextEditSingleActivity.BUDLE_EXTA_CONTENT);

        setContentView(R.layout.activity_bind_alipay);

        tvGobind  = (TextView)findViewById(R.id.id_gobind);
        htevName  = (HorizTextEditView)findViewById(R.id.id_alibind_name);
        htevAliaccount  = (HorizTextEditView)findViewById(R.id.id_alibind_account);
//        htevMobile  = (HorizTextEditView)findViewById(R.id.id_alibind_mobile);
//        hteevSms  = (HorizTextEditExView)findViewById(R.id.id_invate_code);
        setTitleById(R.string.bindalipay_title);

        tvGobind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = htevName.getTip();
                String ali = htevAliaccount.getTip();

                if(StringUtils.isEmpty(name)){
                    ToastUtils.showToast(BindAlipayActivity.this,"姓名不能为空");
                    return;
                }
                if(StringUtils.isEmpty(ali) || !(AppUtils.checkEmail(ali) || AppUtils.isMobileNum(ali))){
                    ToastUtils.showToast(BindAlipayActivity.this,"支付宝账号不合法");
                    return;
                }

                if(dlg==null)
                    dlg = new BindAlipayConfirmDlg(BindAlipayActivity.this,click);
                dlg.show();
            }
        });

        if(StringUtils.isNotEmpty(aliName)) {
            htevAliaccount.setTip(aliName);
        }
    }

    public View.OnClickListener click = new View.OnClickListener(){
        public void onClick(View v){
            bindAli();
            dlg.dismiss();
        }
    };

    @Override
    public void finish(){
        if(StringUtils.isNotEmpty(modStr)){
            Intent data = new Intent().putExtra("content", htevAliaccount.getTip());
            setResult(RESULT_OK, data);
        }
        super.finish();
    }

    private void bindAli(){
        String name = htevName.getTip();
        String ali = htevAliaccount.getTip();

        Retrofit retrofit = RetrofitClient.getInstance();
        IBindAliRequest request = retrofit.create(IBindAliRequest.class);
        final BindAliRequest req = new BindAliRequest();
        req.setRealName(name);
        req.setAlipay(ali);
        Call<FairyApiResult> call = request.getCall(req.getParamsMap());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode()== ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(BindAlipayActivity.this,"绑定成功！");
                    modStr = htevAliaccount.getTip();
                    finish();
                    return;
                }else{
                    ToastUtils.showToast(BindAlipayActivity.this,response.body().getMsg());
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<FairyApiResult> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }

}
