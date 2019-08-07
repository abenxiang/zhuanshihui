package com.sina.shopguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.Income;
import com.sina.shopguide.dto.UserInfo;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.IncomeRequest;
import com.sina.shopguide.net.requestinterface.IIncomeRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppConst;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.view.TopPanelTipView;
import com.sina.shopguide.view.VerDoubleTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class ProfitManageActivity extends BaseActivity {
    public static final String USER_INF="user_info";
    private TextView tvLeftCount;
    private TextView tvWithOpr;
    private TextView tvAllProfit;
    private UserInfo uInfo;

    private Income income;

    private VerDoubleTextView vdtvLastMonthIncom;
    private VerDoubleTextView vdtvCurrMonthIncom;
    private VerDoubleTextView vdtvLastMonthProxyIncom;

    private VerDoubleTextView vdtvTodayPaycount;
    private VerDoubleTextView vdtvTodayMayIncome;
    private VerDoubleTextView vdtvTodayProxyIncome;

    private VerDoubleTextView vdtvYesterdayPaycount;
    private VerDoubleTextView vdtvYesterdayMayIncome;
    private VerDoubleTextView vdtvYesterdayProxyIncome;

    private VerDoubleTextView vdtvSevendayPaycount;
    private VerDoubleTextView vdtvSevendayMayIncome;
    private VerDoubleTextView vdtvSevendayProxyIncome;
    private TopPanelTipView tptvDetail;
    private TopPanelTipView tptvHistory;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profitmanager);
        uInfo = (UserInfo)getIntent().getSerializableExtra(USER_INF);
        tvWithOpr = (TextView) findViewById(R.id.id_profit_withdraw_op);
        tvLeftCount = (TextView) findViewById(R.id.id_profit_leftcontent);
        tvAllProfit = (TextView) findViewById(R.id.id_profit_all);

        vdtvLastMonthIncom = (VerDoubleTextView) findViewById(R.id.id_month_earn);
        vdtvCurrMonthIncom = (VerDoubleTextView) findViewById(R.id.id_today_earn);
        vdtvLastMonthProxyIncom = (VerDoubleTextView) findViewById(R.id.id_mygroup);

        vdtvTodayPaycount = (VerDoubleTextView) findViewById(R.id.id_today_paycount);
        vdtvTodayMayIncome = (VerDoubleTextView) findViewById(R.id.id_today_income);
        vdtvTodayProxyIncome = (VerDoubleTextView) findViewById(R.id.id_today_proxy);

        vdtvYesterdayPaycount = (VerDoubleTextView) findViewById(R.id.id_yesterday_paycount);
        vdtvYesterdayMayIncome = (VerDoubleTextView) findViewById(R.id.id_yesterday_income);
        vdtvYesterdayProxyIncome = (VerDoubleTextView) findViewById(R.id.id_yesterday_proxy);

        vdtvSevendayPaycount = (VerDoubleTextView) findViewById(R.id.id_senvenday_paycount);
        vdtvSevendayMayIncome = (VerDoubleTextView) findViewById(R.id.id_senvenday_income);
        vdtvSevendayProxyIncome = (VerDoubleTextView) findViewById(R.id.id_senvenday_proxy);
        tptvDetail = (TopPanelTipView) findViewById(R.id.id_withdraw_detail);
        tptvHistory = (TopPanelTipView) findViewById(R.id.id_withdraw_history);

        tvWithOpr.setOnClickListener(clickListener);
        tptvDetail.setOnClickListener(clickListener);
        tptvHistory.setOnClickListener(clickListener);
        findViewById(R.id.title_right_opr).setOnClickListener(clickListener);
        getIncome();
    }

    private void updateIncomeUi() {
        tvLeftCount.setText(income.getAmount());
        tvAllProfit.setText("累计结算收入：¥"+income.getAll_amount());

        vdtvLastMonthIncom.setTip("¥"+income.getLast_month_commision());
        vdtvCurrMonthIncom.setTip("¥"+income.getThis_month_commision());
        vdtvLastMonthProxyIncom.setTip("¥"+income.getLast_day_partner_commision());

        vdtvTodayPaycount.setTip("¥"+income.getToday_order_count());
        vdtvTodayMayIncome.setTip("¥"+income.getToday_commision());
        vdtvTodayProxyIncome.setTip(income.getToday_partner_commision());

        vdtvYesterdayPaycount.setTip("¥"+income.getLast_day_order_count());
        vdtvYesterdayMayIncome.setTip("¥"+income.getLast_day_commision());
        vdtvYesterdayProxyIncome.setTip("¥"+income.getLast_day_partner_commision());

        vdtvSevendayPaycount.setTip("¥"+income.getSeven_days_order_count());
        vdtvSevendayMayIncome.setTip("¥"+income.getSeven_days_commision());
        vdtvSevendayProxyIncome.setTip("¥"+income.getSeven_days_partner_commision());
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
                    ToastUtils.showToast(ProfitManageActivity.this, response.body().getMsg());//response.body().getCode());
                    return;
                }
                income = response.body().getData();
                updateIncomeUi();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<CommonApiResult<Income>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println(throwable.getMessage());
            }
        });
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.id_withdraw_detail:
                    ActivityUtils.gotoNextActivity(ProfitManageActivity.this,CommissionSettleActivity.class);
                    break;

                case R.id.id_withdraw_history:
                    ActivityUtils.gotoNextActivity(ProfitManageActivity.this,WithDrawDetailActivity.class);
                    break;

                case R.id.id_profit_withdraw_op:
                    //ActivityUtils.gotoNextActivity(ProfitManageActivity.this,WithDrawActivity.class);
                    Intent in = new Intent(ProfitManageActivity.this, WithDrawActivity.class);
                    if(uInfo!=null)
                        in.putExtra(ProfitManageActivity.USER_INF,uInfo);
                    startActivity(in);
                    break;

                case R.id.title_right_opr:
                    ActivityUtils.goWebActivity(ProfitManageActivity.this, AppConst.APP_FRESHGUIDE_URL);
                    break;
            }
        }
    };
}
