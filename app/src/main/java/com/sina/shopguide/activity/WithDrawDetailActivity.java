package com.sina.shopguide.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.WithDrawItem;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.WithdrawListRequest;
import com.sina.shopguide.net.requestinterface.IWithdrawListRequest;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.net.result.ListApiResult;
import com.sina.shopguide.view.WithDrawItemView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class WithDrawDetailActivity extends BaseActivity {

    private PullToRefreshListView prflView;

    private List<WithDrawItem> lstWithDraw = new ArrayList<WithDrawItem>();
    private WithdrawListAdapter withdrawListAdapter;
    private int page = 1;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_detail);

        prflView = (PullToRefreshListView)findViewById(R.id.id_list_view);
        setTitleById(R.string.withdraw_detail_title);
        withdrawListAdapter = new WithdrawListAdapter();
        prflView.setAdapter(withdrawListAdapter);

        prflView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList();
            }
        });
        getList();
    }


    private void getList() {
        Retrofit retrofit = RetrofitClient.getInstance();

        IWithdrawListRequest request = retrofit.create(IWithdrawListRequest.class);
        final WithdrawListRequest params = new WithdrawListRequest();
        params.setPage(page);

        Call<ListApiResult<WithDrawItem>> call = request.getCall(params.getParamsMap());

        call.enqueue(new Callback<ListApiResult<WithDrawItem>>() {

            @Override
            public void onResponse(Call<ListApiResult<WithDrawItem>> call, Response<ListApiResult<WithDrawItem>> response) {

                if(response.body().getCode() != ErrorCode.SUCCESS.getCode()) {
                    return;
                }

                if(page == 1) {
                    lstWithDraw.clear();
                }
                lstWithDraw.addAll(response.body().getData());
                withdrawListAdapter.notifyDataSetChanged();

                page++;

                prflView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<ListApiResult<WithDrawItem>> call, Throwable throwable) {
                prflView.onRefreshComplete();
                throwable.printStackTrace();
            }
        });
    }

    private class WithdrawListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lstWithDraw.size();
        }

        @Override
        public Object getItem(int position) {
            return lstWithDraw.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WithDrawItemView v = (WithDrawItemView) convertView;
            if(v == null) {
                v = new WithDrawItemView(WithDrawDetailActivity.this,true);
            }

            v.update(lstWithDraw.get(position));
            return v;
        }
    }
}
