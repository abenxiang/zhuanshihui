package com.sina.shopguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.ProductDetailActivity;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ProductListParams;
import com.sina.shopguide.net.requestinterface.IProductListRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.HomeHeaderView;
import com.sina.shopguide.view.ProductItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private PullToRefreshListView lvProduct;

    private HomeHeaderView homeHeaderView;

    private ImageView ivGoTop;

    private int page = 1;

    private List<Product> productList = new ArrayList<>();

    private ProductListAdapter productListAdapter = new ProductListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeHeaderView = new HomeHeaderView(getActivity());

        lvProduct = (PullToRefreshListView) view.findViewById(R.id.lv_product);
        lvProduct.getRefreshableView().addHeaderView(homeHeaderView);
        lvProduct.setAdapter(productListAdapter);

        lvProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getProductList(page + 1);
            }
        });

        ivGoTop = (ImageView) view.findViewById(R.id.iv_go_top);
        ivGoTop.setOnClickListener(this);

        lvProduct.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ivGoTop.setVisibility(firstVisibleItem <= 0 ? View.GONE : View.VISIBLE);
            }
        });

        getProductList(1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void reload(String tab) {

    }

    public void refresh() {
        getProductList(1);
        if(homeHeaderView != null) {
            homeHeaderView.loadData();
        }
    }

    private void getProductList(final int page) {
        if(lvProduct == null) {
            return;
        }

        Retrofit retrofit = RetrofitClient.getInstance();

        IProductListRequest request = retrofit.create(IProductListRequest.class);
        final ProductListParams params = new ProductListParams();
        params.setPage(page);
        if(page == 1) {
            lvProduct.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            lvProduct.setRefreshing();
        }

        final Map<String, String> paramsMap;
        if(UserPreferences.isLogin()) {
            paramsMap = params.getParamsMap();
        }
        else {
            paramsMap = params.getParamsMapMd5Env();
        }
        Call<CommonApiResult<List<Product>>> call = request.getCall(paramsMap);

        call.enqueue(new Callback<CommonApiResult<List<Product>>>() {

            @Override
            public void onResponse(Call<CommonApiResult<List<Product>>> call, Response<CommonApiResult<List<Product>>> response) {
                CommonApiResult<List<Product>> result = response.body();
                int resultCode = result.getCode();
                if(resultCode == ErrorCode.USER_NOT_EXIST.getCode()
                        || resultCode == ErrorCode.EXPIRE_TOKEN.getCode()
                        || resultCode == ErrorCode.INVALID_TOKEN.getCode()) {
                    UserPreferences.setLogin(false);
                    lvProduct.post(new Runnable() {
                        @Override
                        public void run() {
                            getProductList(page);
                        }
                    });
                    return;
                }

                if(resultCode != ErrorCode.SUCCESS.getCode()) {
                    return;
                }

                if(page == 1) {
                    productList.clear();
                }
                productList.addAll(response.body().getData());
                productListAdapter.notifyDataSetChanged();

                HomeFragment.this.page = page;

                lvProduct.setMode(PullToRefreshBase.Mode.BOTH);
                lvProduct.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<CommonApiResult<List<Product>>> call, Throwable throwable) {
                lvProduct.setMode(PullToRefreshBase.Mode.BOTH);
                lvProduct.onRefreshComplete();
                throwable.printStackTrace();
            }
        });
    }

    public void scrollToHead() {
        lvProduct.getRefreshableView().setSelection(0);
        getProductList(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go_top:
                lvProduct.getRefreshableView().setSelection(0);
                break;
        }
    }

    private class ProductListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (productList.size() + 1) / 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = lf.inflate(R.layout.vw_produt_two_item, parent, false);
            }

            ProductItemView leftView = (ProductItemView) v.findViewById(R.id.v_left_product);
            leftView.update(productList.get(position * 2));

            ProductItemView rightView = (ProductItemView) v.findViewById(R.id.v_right_product);
            if((position * 2 + 1) < productList.size()) {
                rightView.setVisibility(View.VISIBLE);
                rightView.update(productList.get(position * 2 + 1));
            }
            else {
                rightView.setVisibility(View.INVISIBLE);
            }

            return v;
        }
    }
}
