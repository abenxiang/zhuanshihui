package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.ImageGalleryActivity;
import com.sina.shopguide.dto.Product;
import com.sina.shopguide.util.ImageLoaderUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by XiangWei on 18/5/16.
 */

public class DetailBannerView extends FrameLayout implements ViewPager.OnPageChangeListener{

    private TextView tvFlag;

    private ViewPager vpBanner;

    private HomeBannerAdapter bannerAdapter;

    private Product product = new Product();

    public DetailBannerView(@NonNull Context context) {
        super(context);

        init();
    }

    public DetailBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public DetailBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public DetailBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.vw_detail_banner, this);

        tvFlag = (TextView) findViewById(R.id.tv_flag);

        vpBanner = (ViewPager) findViewById(R.id.vp_banner);
        bannerAdapter = new HomeBannerAdapter();
        vpBanner.setAdapter(bannerAdapter);

        vpBanner.addOnPageChangeListener(this);
    }

    private class HomeBannerAdapter extends PagerAdapter {

        private SparseArray<WeakReference<ImageView>> viewCacheMap = new SparseArray<WeakReference<ImageView>>();;

        @Override
        public int getCount() {
            if (isViewPagerShownInLoop()) {
                return product.getPic().size() + 2;
            }
            return product.getPic().size();
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int dataPosition = getDataPostion(position);

            ImageView v = null;
            WeakReference<ImageView> viewRef = viewCacheMap.get(position);
            if (viewRef != null) {
                v = viewRef.get();
            }
            if (v == null) {
                v = new ImageView(getContext());
                ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                v.setLayoutParams(lp);
                v.setScaleType(ImageView.ScaleType.CENTER_CROP);
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openBigPic();
                    }
                });
                viewCacheMap.put(position, new WeakReference<ImageView>(v));
            }
            final String pic = product.getPic().get(dataPosition);
            v.setTag(pic);
            ImageLoader.getInstance().displayImage(pic, v, ImageLoaderUtils.getDefaultoptions());
            container.addView(v, 0);
            return v;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (isViewPagerShownInLoop()) {
                final int pos = vpBanner.getCurrentItem();
                if (pos == 0 || pos == bannerAdapter.getCount() - 1) {
                    vpBanner.post(new Runnable() {
                        @Override
                        public void run() {
                            vpBanner.setCurrentItem(getTargetItemPostion(pos), false);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int pos, float arg1, int arg2) {
        // do nothing
    }

    @Override
    public void onPageSelected(final int pos) {
        checkPoint(getTargetItemPostion(pos));
    }

//    private void addPoint(int amount) {
//        llSelectFlagContaner.removeAllViews();
//        for (int i = 0; i < amount; ++i) {
//            ImageView pointView = new ImageView(getContext());
//            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
//                    RadioGroup.LayoutParams.WRAP_CONTENT,
//                    RadioGroup.LayoutParams.WRAP_CONTENT);
//            final int leftMargin = (i == 0) ? 0 : getResources().getDimensionPixelSize(R.dimen.banner_point_space);
//            params.setMargins(leftMargin, 0, 0, 0);
//            pointView.setLayoutParams(params);
//            pointView.setImageDrawable(getResources().getDrawable(R.drawable.bannner_point));
//
//            llSelectFlagContaner.addView(pointView);
//        }
//    }

    private void checkPoint(int pos) {
        tvFlag.setText(String.format("%d/%d", pos, product.getPic().size()));
    }


    /**
     * 是否循环显示ViewPager的内容
     *
     * @return
     */
    boolean isViewPagerShownInLoop() {
        return product.getPic() != null && product.getPic().size() > 1;
    }

    /**
     * ViewPager循环显示时, 获取数据所在的位置
     *
     * @param position
     * @return
     */
    int getDataPostion(int position) {
        if (isViewPagerShownInLoop() && !product.getPic().isEmpty()) {
            return (position + product.getPic().size() - 1) % product.getPic().size();
        }

        return position;
    }

    /**
     * ViewPager循环显示时, 获取可能要跳转的位置(如果输入和输出不等, 则ViewPage需要跳转到输出的位置)
     * <p>
     * ****************************************************************
     * Data:        |  2  |  0  |  1  |  2  |  0  |
     * Item:        |  0  |  1  |  2  |  3  |  4  |
     * TargetItem:  |  3  |  1  |  2  |  3  |  1  |
     * ****************************************************************
     *
     * @param position
     * @return
     */
    int getTargetItemPostion(int position) {
        if (isViewPagerShownInLoop() && !product.getPic().isEmpty()) {
            if (position == 0) {
                position += product.getPic().size();
            } else if (position == product.getPic().size() + 1) {
                position -= product.getPic().size();
            }
        }

        return position;
    }

    public void update(Product p) {
        if(p == null) {
            return;
        }

        this.product = p;
        bannerAdapter.notifyDataSetChanged();

        checkPoint(1);
        vpBanner.setCurrentItem(1, false);
    }

    private void openBigPic() {
        ArrayList<String> imgs = new ArrayList<>();
        imgs.addAll(product.getPic());
        Intent intent = new Intent(getContext(), ImageGalleryActivity.class);
        intent.putStringArrayListExtra(ImageGalleryActivity.EXTRA_KEY_IMAGE_LIST, imgs);
        intent.putExtra(ImageGalleryActivity.EXTRA_KEY_CURRENT_INDEX, getDataPostion(vpBanner.getCurrentItem()));
        getContext().startActivity(intent);
    }
}
