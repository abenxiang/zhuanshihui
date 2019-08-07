package com.sina.shopguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.internal.FlipLoadingLayout;
import com.sina.shopguide.R;
import com.sina.shopguide.dialog.BindAlipayConfirmDlg;
import com.sina.shopguide.dto.Income;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.dto.WithDrawItem;
import com.sina.shopguide.fragment.MeFragment;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.IncomeRequest;
import com.sina.shopguide.net.request.WithdrawRequest;
import com.sina.shopguide.net.requestinterface.IIncomeRequest;
import com.sina.shopguide.net.requestinterface.IWithdrawRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.FairyApiResult;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.view.HorizTextEditView;
import com.sina.shopguide.view.SettingView;
import com.sina.shopguide.view.TopPanelTipView;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class WithDrawActivity extends BaseActivity {
    public static final String TOTAL_NUM="total_num";
    private TextView tvDetail;
    private TextView tvWithOpr;
    private TextView tvTotal;

    private TopPanelTipView tptvAliaccount;
    private HorizTextEditView htevCurrNum;
    private BindAlipayConfirmDlg dlg;

    private UserInfo uInfo;
    private Income income;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        //total = getIntent().getFloatExtra(TOTAL_NUM,0.00f);
        uInfo = (UserInfo)getIntent().getSerializableExtra(ProfitManageActivity.USER_INF);
        tvDetail = (TextView)findViewById(R.id.title_right_opr);
        tvDetail.setVisibility(View.VISIBLE);
        tvDetail.setText(R.string.withdraw_detail);

        tvWithOpr  = (TextView)findViewById(R.id.id_withdraw_opr);

        tvTotal  = (TextView)findViewById(R.id.id_withdraw_total);
        tptvAliaccount  = (TopPanelTipView)findViewById(R.id.id_aliaccount);
        htevCurrNum  = (HorizTextEditView)findViewById(R.id.id_curr_num);

        if(uInfo!=null){
            if(StringUtils.isNotEmpty(uInfo.getAlipay())){
                tptvAliaccount.setTip(uInfo.getAlipay());
            }
        }
       // tvTotal.setText(String.valueOf(total));
        setTitleById(R.string.withdraw_title);

        tptvAliaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dlg == null)
                    dlg = new BindAlipayConfirmDlg(WithDrawActivity.this,click,getString(R.string.dlg_rebindcomfrim_tip));

                dlg.show();
            }
        });

        tvWithOpr.setOnClickListener(click);
        tvDetail.setOnClickListener(click);
        getIncome();
    }


    public void getIncome() {
        Retrofit retrofit = RetrofitClient.getInstance();
        IIncomeRequest request = retrofit.create(IIncomeRequest.class);
        final IncomeRequest req = new IncomeRequest();

        Call<CommonApiResult<Income>> call = request.getCall(req.getParamsMap());//"I love you");

        call.enqueue(new Callback<CommonApiResult<Income>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<CommonApiResult<Income>> call, Response<CommonApiResult<Income>> response) {

                if (response.body().getCode() != ErrorCode.SUCCESS.getCode()) {
                    ToastUtils.showToast(WithDrawActivity.this, response.body().getMsg());//response.body().getCode());
                    return;
                }
                income = response.body().getData();
                //updateIncomeUi();
                tvTotal.setText(String.valueOf(income.getAmount()));
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<CommonApiResult<Income>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }

    public View.OnClickListener click = new View.OnClickListener(){
        public void onClick(View v){
            int id = v.getId();
            switch(id){
                case R.id.id_withdraw_opr:
                    withDraw();
                    break;
                case R.id.title_right_opr:
                    Intent in = new Intent(WithDrawActivity.this, WithDrawDetailActivity.class);
                    startActivity(in);
                    break;
                default:
                    bindAli();
                    dlg.dismiss();
                    break;
            }
        }
    };

    private void bindAli(){
        Intent in = new Intent(WithDrawActivity.this,BindAlipayActivity.class);
        in.putExtra(TextEditSingleActivity.BUDLE_EXTA_CONTENT,tptvAliaccount.getTip());
        startActivityForResult(in, SettingView.REQUESTCODE_ALI);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String content = null;
        SimpleDebugPrinterUtils.println("gggggg   333 requestCode" + requestCode);
        if(data!=null && data.hasExtra("content")) {
            content = data.getStringExtra("content");
        }
        switch (requestCode) {
            case SettingView.REQUESTCODE_ALI:
                tptvAliaccount.setTip(content);
                break;
        }
    }

    private void withDraw(){
        String num = htevCurrNum.getTip();
        if(StringUtils.isEmpty(num)){
            ToastUtils.showToast(WithDrawActivity.this,"提现金额不能为空");
            return;
        }

        if(Float.parseFloat(num)>Float.parseFloat(income.getAmount())){
            ToastUtils.showToast(WithDrawActivity.this,"提现金额不能高于可提现额度");
            return;
        }

        Retrofit retrofit = RetrofitClient.getInstance();
        IWithdrawRequest request = retrofit.create(IWithdrawRequest.class);
        final WithdrawRequest req = new WithdrawRequest();
        req.setAmount(num);
        Call<FairyApiResult> call = request.getCall(req.getParamsMap());

        call.enqueue(new Callback<FairyApiResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<FairyApiResult> call, Response<FairyApiResult> response) {
                if(response.body().getCode()== ErrorCode.SUCCESS.getCode()){
                    ToastUtils.showToast(WithDrawActivity.this,getString(R.string.toast_withdraw_sucess));
                    return;
                }else{
                    ToastUtils.showToast(WithDrawActivity.this,response.body().getMsg());
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
