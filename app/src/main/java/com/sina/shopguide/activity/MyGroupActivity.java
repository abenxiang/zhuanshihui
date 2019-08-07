package com.sina.shopguide.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.Group;
import com.sina.shopguide.dto.GroupListMore;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.GroupListRequest;
import com.sina.shopguide.net.requestinterface.IGroupListRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.ToastUtils;
import com.sina.shopguide.view.GroupItemView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class MyGroupActivity extends BaseActivity {

    private PullToRefreshListView prflView;
    private TextView tvParnerA;
    private TextView tvParnerB;
    private GroupListAdapter groupListAdapter;
    private List<Group> lstGroupA=new ArrayList<Group>();
    private List<Group> lstGroupB=new ArrayList<Group>();

    private int page=1;
    private int currType = GroupListRequest.TYPE_PARNER_A;
    private int LastType = GroupListRequest.TYPE_PARNER_A;
    private View tvDivALine;
    private View tvDivBLine;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygroup);

        prflView = (PullToRefreshListView)findViewById(R.id.id_list_view);

        tvParnerA = (TextView)findViewById(R.id.divider_parner_a);
        tvParnerB = (TextView)findViewById(R.id.divider_parner_b);

        tvDivALine = (View) findViewById(R.id.divider_a_line);
        tvDivALine.setVisibility(View.VISIBLE);
        tvDivBLine = (View) findViewById(R.id.divider_b_line);
        tvDivBLine.setVisibility(View.GONE);

        setTitleById(R.string.mygroup_title);
        groupListAdapter = new GroupListAdapter(lstGroupA);
        prflView.setAdapter(groupListAdapter);

        prflView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getList(currType);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(currType != LastType){
                    page = 1;
                    LastType = currType;
                }
                getList(currType);
            }
        });
        getList(currType);
        tvParnerA.setOnClickListener(clickListener);
        tvParnerB.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.divider_parner_a:
                    update(GroupListRequest.TYPE_PARNER_A);
                    break;
                case R.id.divider_parner_b:
                    update(GroupListRequest.TYPE_PARNER_B);
                    break;
            }
        }
    };

    private void update(int type){
        currType = type;
        page = 1;

        if(type == GroupListRequest.TYPE_PARNER_A){
            tvDivALine.setVisibility(View.VISIBLE);
            tvDivBLine.setVisibility(View.GONE);
            tvParnerA.setTextColor(Color.parseColor("#FD4A41"));
            tvParnerB.setTextColor(Color.parseColor("#282428"));
            groupListAdapter.setGroups(lstGroupA);
        }else{
            tvDivALine.setVisibility(View.GONE);
            tvParnerB.setTextColor(Color.parseColor("#FD4A41"));
            tvParnerA.setTextColor(Color.parseColor("#282428"));
            tvDivBLine.setVisibility(View.VISIBLE);
            groupListAdapter.setGroups(lstGroupB);
        }

        getList(currType);
    }

    private void getList(final int type) {
        Retrofit retrofit = RetrofitClient.getInstance();

        IGroupListRequest request = retrofit.create(IGroupListRequest.class);
        final GroupListRequest params = new GroupListRequest();
        params.setType(type);
        params.setPage(page);

        Call<CommonApiResult<GroupListMore>> call = request.getCall(params.getParamsMap());

        call.enqueue(new Callback<CommonApiResult<GroupListMore>>() {

            @Override
            public void onResponse(Call<CommonApiResult<GroupListMore>> call, Response<CommonApiResult<GroupListMore>> response) {

                if(response.body().getCode() != ErrorCode.SUCCESS.getCode()) {
                    ToastUtils.showToast(MyGroupActivity.this,response.body().getMsg());
                    return;
                }

                List<Group> result = response.body().getData().getLists();
                tvParnerA.setText("A级合伙人("+response.body().getData().getNums1()+")");
                tvParnerB.setText("B级合伙人("+response.body().getData().getNums2()+")");
                if(type == GroupListRequest.TYPE_PARNER_A) {
                    if (page == 1) {
                        lstGroupA.clear();
                    }
                    if(response.body().getData()!=null && response.body().getData().getLists()!=null
                            && response.body().getData().getLists().size()!=0)
                        lstGroupA.addAll(response.body().getData().getLists());
                }else{
                    if (page == 1) {
                        lstGroupB.clear();
                    }
                    if(response.body().getData()!=null && response.body().getData().getLists()!=null
                            && response.body().getData().getLists().size()!=0)
                        lstGroupB.addAll(response.body().getData().getLists());
                }
                groupListAdapter.notifyDataSetChanged();

                page++;

                prflView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<CommonApiResult<GroupListMore>> call, Throwable throwable) {
                prflView.onRefreshComplete();
                throwable.printStackTrace();
            }
        });
    }

    private class GroupListAdapter extends BaseAdapter {
        List<Group> groups ;
        GroupListAdapter(List<Group> goup){
            groups = goup;
        }

        public void setGroups(List<Group> goup){
            groups = goup;
        }
        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public Object getItem(int position) {
            return groups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GroupItemView v = (GroupItemView) convertView;
            if(v == null) {
                v = new GroupItemView(MyGroupActivity.this);
            }

            v.update(groups.get(position));
            return v;
        }
    }
}
