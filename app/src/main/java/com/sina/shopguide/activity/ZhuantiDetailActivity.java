package com.sina.shopguide.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.dto.SimpleProduct;
import com.sina.shopguide.dto.ZhuantiDetailMore;
import com.sina.shopguide.dto.zhuanti;
import com.sina.shopguide.net.RetrofitClient;
import com.sina.shopguide.net.request.ZhuantiDetailRequest;
import com.sina.shopguide.net.requestinterface.IZhuantiDetailRequest;
import com.sina.shopguide.net.result.CommonApiResult;
import com.sina.shopguide.net.result.ErrorCode;
import com.sina.shopguide.util.DensityUtil;
import com.sina.shopguide.util.ImageLoaderUtils;
import com.sina.shopguide.util.SimpleDebugPrinterUtils;
import com.sina.shopguide.util.UserPreferences;
import com.sina.shopguide.view.ObserveScrollView;
import com.sina.shopguide.view.ProductSimpleItemView;
import com.sina.shopguide.view.ZhuantiItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tiger on 18/5/8.
 */

public class ZhuantiDetailActivity extends BaseActivity {
    public static final String PID="PID";

    private int zhuantiPerHeight;
    private int ProductPerHeight;
    private ImageView ivIcon;
    private ObserveScrollView osWhole;
    private TextView tvTitle;
    private TextView tvDesc;
    private GridView gvNS;
    private ListView lvDesc;

    private String pid;
    private ZhuantiDetailMore zhuantiMore;

    private ProductAdapter pdAdapter;
    private ZhuantiListAdapter zhuantiListAdapter;

    private RelativeLayout rvTitle;

    private int minShowTitle;
    private int maxShowTitle;

    private ImageView ivWhteBack;
    private ImageView ivGoTop;
    private TextView tvNvshen;
    public boolean firstUI=true;

    private List<SimpleProduct> lstProduct = new ArrayList<SimpleProduct>();
    private List<zhuanti> lstZhuanti = new ArrayList<zhuanti>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuanti_detail);
        pid = getIntent().getStringExtra(PID);
        pdAdapter = new ProductAdapter();
        firstUI = true;
        zhuantiListAdapter = new ZhuantiListAdapter();
        zhuantiPerHeight = DensityUtil.dip2px(ZhuantiDetailActivity.this,230);
        ProductPerHeight = DensityUtil.dip2px(ZhuantiDetailActivity.this,250);
        minShowTitle = DensityUtil.dip2px(ZhuantiDetailActivity.this,220);
        maxShowTitle = DensityUtil.dip2px(ZhuantiDetailActivity.this,260);
        ivWhteBack  = (ImageView)findViewById(R.id.iv_white_back);
        ivGoTop  = (ImageView)findViewById(R.id.iv_go_top);
        rvTitle  = (RelativeLayout)findViewById(R.id.rv_titles);
        osWhole  = (ObserveScrollView)findViewById(R.id.sv_whole);
        ivIcon  = (ImageView)findViewById(R.id.id_icon);
        tvTitle  = (TextView)findViewById(R.id.tv_title);
        tvNvshen  = (TextView)findViewById(R.id.tv_nvshen);

        tvDesc  = (TextView)findViewById(R.id.tv_desc);
        gvNS  = (GridView)findViewById(R.id.gv_nvshen);
        lvDesc  = (ListView)findViewById(R.id.lv_more);
        gvNS.setAdapter(pdAdapter);
        lvDesc.setAdapter(zhuantiListAdapter);
        osWhole.setScrollListener(new ObserveScrollView.ScrollListener() {

            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                // TODO Auto-generated method stub
                //滑动数据已经接收，在这里实现你的功能
                SimpleDebugPrinterUtils.println("gggggg g l"+l+"  t:"+t+"  oldl:"+l+"  oldt:"+t+ " firstUI:"+firstUI);
                if(firstUI){
                    return;
                }

                if(t>=minShowTitle){
                    rvTitle.setVisibility(View.VISIBLE);
                    ivWhteBack.setVisibility(View.INVISIBLE);
                    ivGoTop.setVisibility(View.VISIBLE);
                }else {
                    rvTitle.setVisibility(View.INVISIBLE);
                    ivWhteBack.setVisibility(View.VISIBLE);
                    ivGoTop.setVisibility(View.INVISIBLE);
                }
            }
        });

        ivWhteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivGoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                osWhole.smoothScrollTo(0,0);
                rvTitle.setVisibility(View.INVISIBLE);
                ivWhteBack.setVisibility(View.VISIBLE);
                ivGoTop.setVisibility(View.INVISIBLE);
            }
        });

        getDetailInfo();
    }

    private void updateUI(){
        if(zhuantiMore == null || zhuantiMore.getInfo() == null){
            return;
        }

        if(zhuantiMore.getInfo().getBanner_url() != null && !zhuantiMore.getInfo().getBanner_url().isEmpty()) {
            ImageLoader.getInstance().displayImage(zhuantiMore.getInfo().getBanner_url(), ivIcon, ImageLoaderUtils.getDefaultoptions());
        }

        tvTitle.setText(zhuantiMore.getInfo().getTitle());
        tvDesc.setText(zhuantiMore.getInfo().getDescription());
        setTitle(zhuantiMore.getInfo().getTitle());

        if(zhuantiMore.getRelat()!=null
                && zhuantiMore.getRelat().size()>0) {
            lstZhuanti = zhuantiMore.getRelat();
            lvDesc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lstZhuanti.size() * zhuantiPerHeight));
        }
        if(zhuantiMore.getGproduct()!=null && zhuantiMore.getGproduct().size()>0) {
            tvNvshen.setText(zhuantiMore.getGproduct().get(0).getGroup_name());
            if(zhuantiMore.getGproduct().get(0).getProduct()!=null
                    && zhuantiMore.getGproduct().get(0).getProduct().size()>0) {
                lstProduct = zhuantiMore.getGproduct().get(0).getProduct();
                gvNS.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((lstProduct.size() + 1) / 2) * ProductPerHeight));
            }
            }

        //等待gridview和listview布局完成后重新回到是scollview的（0，0）位置
        gvNS.postDelayed(new Runnable() {
            @Override
            public void run() {
                osWhole.smoothScrollTo(0,0);
                firstUI = false;
            }
        },100);

        pdAdapter.notifyDataSetChanged();

        zhuantiListAdapter.notifyDataSetChanged();
    }

    public void onDestroy(){
        super.onDestroy();
        gvNS.removeCallbacks(null);
    }

    private void getDetailInfo(){
        Retrofit retrofit = RetrofitClient.getInstance();

        IZhuantiDetailRequest request = retrofit.create(IZhuantiDetailRequest.class);
        final ZhuantiDetailRequest params = new ZhuantiDetailRequest();
        params.setTid(pid);

        final Map<String, String> paramsMap;
        if(UserPreferences.isLogin()) {
            paramsMap = params.getParamsMap();
        }
        else {
            paramsMap = params.getParamsMapMd5Env();
        }

        Call<CommonApiResult<ZhuantiDetailMore>> call = request.getCall(paramsMap);

        call.enqueue(new Callback<CommonApiResult<ZhuantiDetailMore>>() {

            @Override
            public void onResponse(Call<CommonApiResult<ZhuantiDetailMore>> call, Response<CommonApiResult<ZhuantiDetailMore>> response) {
                CommonApiResult<ZhuantiDetailMore> result = response.body();
                if(result.getCode() != ErrorCode.SUCCESS.getCode()) {
                    return;
                }

                zhuantiMore = result.getData();
                updateUI();
            }

            @Override
            public void onFailure(Call<CommonApiResult<ZhuantiDetailMore>> call, Throwable throwable) {
                SimpleDebugPrinterUtils.println("ggggggg  faile"+throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    private class ProductAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return lstProduct.size();
        }

        @Override
        public Object getItem(int position) {
            return lstProduct.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProductSimpleItemView v = (ProductSimpleItemView) convertView;

            if(v == null) {
                v = new ProductSimpleItemView(ZhuantiDetailActivity.this);
            }
            v.update(lstProduct.get(position));
            return v;
        }
    }

    private class ZhuantiListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lstZhuanti.size();
        }

        @Override
        public Object getItem(int position) {
            return lstZhuanti.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ZhuantiItemView v = (ZhuantiItemView) convertView;
            if(v == null) {
                v = new ZhuantiItemView(ZhuantiDetailActivity.this);
            }
            v.setVVisible();
            v.update(lstZhuanti.get(position));
            return v;
        }
    }
}
