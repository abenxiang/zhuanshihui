package com.sina.shopguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.shopguide.R;
import com.sina.shopguide.dialog.ShareProductDialog;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ProductDetailParams;
import com.sina.shopguide.net.requestinterface.IProductDetailRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.AlibcUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.DetailHeaderView;
import com.sina.shopguide.view.ProductSimpleItemView;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by XiangWei on 18/5/16.
 */

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_PRODUCT = "product";

    private static final int REQ_CODE_LOGIN = 0;

    private PullToRefreshListView plvDetail;

    private DetailHeaderView headerView;

    private DetailListAdapter detailListAdapter;

    private TextView tvBuyWithCoupon;

    private TextView tvShare;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        product = (Product) getIntent().getSerializableExtra(EXTRA_PRODUCT);
        loadProduct();

        setContentView(R.layout.activity_product_detail);

        plvDetail = (PullToRefreshListView) findViewById(R.id.plv_detail);
        plvDetail.getRefreshableView().addHeaderView(headerView = new DetailHeaderView(this));
        plvDetail.setAdapter(detailListAdapter = new DetailListAdapter());
        plvDetail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadProduct();
            }
        });

        tvShare = (TextView) findViewById(R.id.tv_share);
        tvBuyWithCoupon = (TextView) findViewById(R.id.tv_buy_with_coupon);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ly_share).setOnClickListener(this);
        findViewById(R.id.ly_buy).setOnClickListener(this);

        updateUI();
    }

    private void loadProduct() {
        if(product != null && product.getId() != 0) {
            getProductDetail(product.getId() + "");
        }
    }

    private void updateUI() {
        headerView.update(product);
        detailListAdapter.notifyDataSetChanged();
        tvBuyWithCoupon.setText(getString(R.string.save_money) + product.getCouponPrice());
        if(UserPreferences.isLogin()) {
            tvShare.setText(getString(R.string.share_with_commision) + product.getCommision());
       }
        else {
            tvShare.setText(getString(R.string.share));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_LOGIN && resultCode == RESULT_OK) {
            loadProduct();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ly_share:
                if(UserPreferences.isLogin()) {
                    showShareProductDialog();
                }
                else {
                    goToLogin();
                }
                break;

            case R.id.ly_buy:
                if(UserPreferences.isLogin()) {
                    if(product != null) {
                        openTaobao(product.getCouponClickUrl());
                    }
                }
                else {
                    goToLogin();
                }
                break;
        }
    }

    private void goToLogin() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQ_CODE_LOGIN);
    }

    private void openTaobao(String url) {
        if(StringUtils.isEmpty(url)) {
            return;
        }

        AlibcUtils.openTaobaoUrl(this, url);
    }

    private ShareProductDialog shareProductDialog;

    private void showShareProductDialog() {
        if(shareProductDialog == null) {
            shareProductDialog = new ShareProductDialog(this);
        }
        shareProductDialog.setProduct(product);
        shareProductDialog.show();
    }

    private void getProductDetail(final String pid) {
        Retrofit retrofit = RetrofitClient.getInstance();

        IProductDetailRequest request = retrofit.create(IProductDetailRequest.class);
        final ProductDetailParams params = new ProductDetailParams();
        params.setPid(pid);

        final Map<String, String> paramsMap;
        if(UserPreferences.isLogin()) {
            paramsMap = params.getParamsMap();
        }
        else {
            paramsMap = params.getParamsMapMd5Env();
        }

        Call<CommonApiResult<Product>> call = request.getCall(paramsMap);

        call.enqueue(new Callback<CommonApiResult<Product>>() {

            @Override
            public void onResponse(Call<CommonApiResult<Product>> call, Response<CommonApiResult<Product>> response) {
                plvDetail.onRefreshComplete();
                CommonApiResult<Product> result = response.body();
                if(result.getCode() != ErrorCode.SUCCESS.getCode()) {
                    return;
                }

                product = result.getData();
                updateUI();
            }

            @Override
            public void onFailure(Call<CommonApiResult<Product>> call, Throwable throwable) {
                plvDetail.onRefreshComplete();
                throwable.printStackTrace();
            }
        });
    }

    private class DetailListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(product != null && product.getRelatedProduct() != null) {
                return (product.getRelatedProduct().size() + 1) / 2;
            }
            return 0;
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
                v = getLayoutInflater().inflate(R.layout.vw_simple_produt_two_item, parent, false);
            }

            ProductSimpleItemView leftP = (ProductSimpleItemView) v.findViewById(R.id.v_left_product);
            leftP.update(product.getRelatedProduct().get(position * 2));

            ProductSimpleItemView rightP = (ProductSimpleItemView) v.findViewById(R.id.v_right_product);
            if((position * 2 + 1) < product.getRelatedProduct().size()) {
                rightP.setVisibility(View.VISIBLE);
                rightP.update(product.getRelatedProduct().get(position * 2 + 1));
            }
            else {
                rightP.setVisibility(View.INVISIBLE);
            }

            return v;
        }
    }
}
