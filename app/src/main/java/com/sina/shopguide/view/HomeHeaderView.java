package com.sina.shopguide.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.HomeTopInfo;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.FairyRequestParams;
import com.sina.shopguide.net.requestinterface.IHomeTopRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.UserPreferences;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by XiangWei on 18/7/25.
 */

public class HomeHeaderView extends FrameLayout {

    private HomeBannerView vHomeBanner;

    private HomeSelectView vHomeSelect;

    private HomeTopicView vHomeTopic;

    public HomeHeaderView(@NonNull Context context) {
        super(context);

        init();
    }

    public HomeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public HomeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public HomeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_home_header, this);

        vHomeBanner = (HomeBannerView) findViewById(R.id.v_home_banner);
        vHomeSelect = (HomeSelectView) findViewById(R.id.v_home_select);
        vHomeTopic = (HomeTopicView) findViewById(R.id.v_home_topic);

        loadData();
    }


    public void loadData() {
        Retrofit retrofit = RetrofitClient.getInstance();

        IHomeTopRequest request = retrofit.create(IHomeTopRequest.class);
        final FairyRequestParams params = new FairyRequestParams();

        final Map<String, String> paramsMap;
        if(UserPreferences.isLogin()) {
            paramsMap = params.getParamsMap();
        }
        else {
            paramsMap = params.getParamsMapMd5Env();
        }
        Call<CommonApiResult<HomeTopInfo>> call = request.getHomeTop(paramsMap);

        call.enqueue(new Callback<CommonApiResult<HomeTopInfo>>() {

            @Override
            public void onResponse(Call<CommonApiResult<HomeTopInfo>> call, Response<CommonApiResult<HomeTopInfo>> response) {
                final int resultCode = response.body().getCode();
                if(resultCode != ErrorCode.SUCCESS.getCode()) {
                    return;
                }

                setVisibility(View.VISIBLE);
                CommonApiResult<HomeTopInfo> result = response.body();
                vHomeBanner.update(result.getData().getBanner());
                vHomeSelect.update(result.getData().getSelect());
                vHomeTopic.update(result.getData().getTopic());
            }

            @Override
            public void onFailure(Call<CommonApiResult<HomeTopInfo>> call, Throwable t) {
                setVisibility(View.GONE);
            }
        });

    }
}
