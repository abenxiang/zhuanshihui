package com.sina.shopguide.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.dto.SearchProduct;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ProductListSearchRequest;
import com.sina.shopguide.net.requestinterface.IProductListSearchRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.ProductItemView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


//搜索结果界面
public class SearchResultActivity extends BaseActivity {
    public static final String SEARCH_CONTENT = "search_content";
    public static final String SEARCH_FROM = "search_from";
    private String sContent;
    private PullToRefreshGridView lvProduct;
    private List<Product> productList = new ArrayList<>();
    private int page = 1;

    private ProductListAdapter productListAdapter = new ProductListAdapter();
    private int from = 0;

    private ImageView ivBackTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_result);
        sContent = getIntent().getStringExtra(SEARCH_CONTENT);
        from = getIntent().getIntExtra(SEARCH_FROM, 0);
        initView();
    }

    public void setEmpty() {
        lvProduct.setVisibility(View.GONE);
        lyEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText("还没有商品哦~");
        ivEmpty.setImageResource(R.drawable.ic_product_search_empty);
    }

    private void getProductList(final int page) {
        Retrofit retrofit = RetrofitClient.getInstance();

        IProductListSearchRequest request = retrofit.create(IProductListSearchRequest.class);
        final ProductListSearchRequest params = new ProductListSearchRequest();
        params.setPage(page);
        if (page == 1) {
            lvProduct.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            lvProduct.setRefreshing();
        }

        params.setKeyword(sContent);

        final Map<String, String> paramsMap;
        if (UserPreferences.isLogin()) {
            paramsMap = params.getParamsMap();
        } else {
            paramsMap = params.getParamsMapMd5Env();
        }
        Call<CommonApiResult<SearchProduct>> call = request.getCall(paramsMap);

        call.enqueue(new Callback<CommonApiResult<SearchProduct>>() {

            @Override
            public void onResponse(Call<CommonApiResult<SearchProduct>> call, Response<CommonApiResult<SearchProduct>> response) {
                CommonApiResult<SearchProduct> result = response.body();

                SimpleDebugPrinterUtils.println("ggggg  succ");
                int resultCode = result.getCode();
                if (resultCode == ErrorCode.USER_NOT_EXIST.getCode()
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

                if (resultCode != ErrorCode.SUCCESS.getCode()) {
                    return;
                }

                if (page == 1) {
                    productList.clear();
                }

                productList.addAll(response.body().getData().getData());
                productListAdapter.notifyDataSetChanged();
                SimpleDebugPrinterUtils.println("ggggg  succ"+productList.size());

                SearchResultActivity.this.page = page;

                lvProduct.setMode(PullToRefreshBase.Mode.BOTH);
                lvProduct.onRefreshComplete();

                if(productList==null || productList.size()==0){
                    setEmpty();
                }
            }

            @Override
            public void onFailure(Call<CommonApiResult<SearchProduct>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println("ggggg  "+throwable.getMessage());
                lvProduct.setMode(PullToRefreshBase.Mode.BOTH);
                lvProduct.onRefreshComplete();
                throwable.printStackTrace();
            }
        });
    }

    private OnScrollListener custScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                    break;
                case OnScrollListener.SCROLL_STATE_FLING://滚动状态
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                    break;
            }
        }

        /**
         * 正在滚动
         * firstVisibleItem第一个Item的位置
         * visibleItemCount 可见的Item的数量
         * totalItemCount item的总数
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem <= 0) {
                ivBackTop.setVisibility(View.GONE);
                return;
            } else {
                ivBackTop.setVisibility(View.VISIBLE);
            }
        }
    };

    private LinearLayout lyEmpty;
    private ImageView ivEmpty;
    private TextView tvEmpty;

    protected void initView() {
        ivEmpty = (ImageView) findViewById(R.id.emp_img);
        tvEmpty = (TextView) findViewById(R.id.emp_text);
        ivBackTop = (ImageView) findViewById(R.id.back_top);
        lvProduct = (PullToRefreshGridView) findViewById(R.id.lv_product);
        lvProduct.setOnScrollListener(custScrollListener);
        lyEmpty = (LinearLayout) findViewById(R.id.comment_empty);

        setTitle(sContent);
        if (from == -1) {
            tvEmpty.setText("您还没有此类商品哦！");
        }

        lvProduct.setAdapter(productListAdapter);

        lvProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                getProductList(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                getProductList(page + 1);
            }
        });

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchResultActivity.this, ProductDetailActivity.class);
                i.putExtra(ProductDetailActivity.EXTRA_PRODUCT, productList.get((int) id));
                startActivity(i);
            }
        });

        getProductList(1);

        ivBackTop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                lvProduct.getRefreshableView().setSelection(0);
            }
        });
    }

    private class ProductListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProductItemView v = (ProductItemView) convertView;
            if (v == null) {
                v = new ProductItemView(SearchResultActivity.this);
            }

            final int spaceWidth = getResources().getDimensionPixelSize(R.dimen.home_grid_item_margin);
            if (position % 2 == 0) {
                v.setPadding(spaceWidth, v.getPaddingTop(), 0, v.getPaddingBottom());
            } else {
                v.setPadding(spaceWidth, v.getPaddingTop(),
                        spaceWidth,
                        v.getPaddingBottom());
            }

            v.update(productList.get(position));
            return v;
        }
    }
}
