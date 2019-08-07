package com.sina.shopguide.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.SearchActivity;
import com.sina.shopguide.activity.ZhuantiDetailActivity;
import com.sina.shopguide.dto.zhuanti;
import com.sina.shopguide.util.ImageLoaderUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页轮播
 */
public class HomeBannerView extends FrameLayout implements
		ViewPager.OnPageChangeListener, IUpdate<List<zhuanti>> {

	private BannerViewPager bannerViewPager;

	private BannerAdapter bannerAdapter;

	private List<zhuanti> boardAreas = new ArrayList<>();

	private View bannerRoot;

	private LinearLayout llSelectFlagContaner;

	public HomeBannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HomeBannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HomeBannerView(Context context) {
		super(context);
		init();
	}

	public void setAlpha(float alpha){
		ivSearch.setAlpha(alpha);
		findViewById(R.id.iv_search).setAlpha(alpha);
		findViewById(R.id.tv_search).setAlpha(alpha);
	}
	public void setNoneSearch(){
		if(ivSearch!=null){
			ivSearch.setVisibility(View.GONE);
		}
	}
	public static final float MAX_OFFSET = 150f;
	public void updateSearchAlpha(float fvalue){
		float ft = Math.abs(fvalue);
		if(ft>=MAX_OFFSET){
			setAlpha(0);
		}else if(ft<MAX_OFFSET && ft>0.1f){
			setAlpha(1f-(ft/MAX_OFFSET));
		}else{
			setAlpha(1f);
		}
	}

	private RelativeLayout ivSearch;
	private void init() {
		inflate(getContext(), R.layout.vw_home_banner, this);
		this.setBackgroundColor(getResources().getColor(R.color.colorWhite));
		ivSearch = (RelativeLayout)findViewById(R.id.ry_search);
		bannerViewPager = (BannerViewPager) findViewById(R.id.banner_pager);
		bannerAdapter = new BannerAdapter();
		bannerViewPager.setAdapter(bannerAdapter);

		bannerViewPager.setOnPageChangeListener(this);

		bannerViewPager.setOnDispatchTouchEventListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					stopAutoPlay();
					break;

				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
					startAutoPlay();
				default:
					break;
				}
				return false;
			}
		});

		bannerRoot = findViewById(R.id.rl_banner_root);
		llSelectFlagContaner = (LinearLayout) findViewById(R.id.ll_select_flag);


		ivSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in = new Intent(getContext(),SearchActivity.class);
				getContext().startActivity(in);
			}
		});
	}

	@Override
	public void update(List<zhuanti> list) {
		boardAreas.clear();
		boardAreas.addAll(list);

		if (boardAreas.isEmpty()) {
			bannerRoot.setVisibility(View.GONE);
		} else {
			bannerRoot.setVisibility(View.VISIBLE);
			bannerAdapter.notifyDataSetChanged();

			addPoint(boardAreas.size());
			bannerViewPager.setCurrentItem(1, false);
			checkPoint(0);
		}

		startAutoPlay();
	}

	private class BannerAdapter extends PagerAdapter {

		private SparseArray<WeakReference<ImageView>> viewCacheMap = new SparseArray<WeakReference<ImageView>>();;

		@Override
		public int getCount() {
			if (isViewPagerShownInLoop()) {
				return boardAreas.size() + 2;
			}
			return boardAreas.size();
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
				v.setScaleType(ScaleType.CENTER_CROP);
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent in = new Intent(getContext(), ZhuantiDetailActivity.class);
						zhuanti z = (zhuanti) v.getTag();
						in.putExtra(ZhuantiDetailActivity.PID, z.getId());
						getContext().startActivity(in);
					}
				});
				viewCacheMap.put(position, new WeakReference<ImageView>(v));
			}
			final zhuanti z = boardAreas.get(dataPosition);
			v.setTag(z);
			ImageLoader.getInstance().displayImage(z.getPicUrl(), v,
					ImageLoaderUtils.getDefaultoptions());
			container.addView(v, 0);
			return v;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			if (isViewPagerShownInLoop()) {
				final int pos = bannerViewPager.getCurrentItem();
				if (pos == 0 || pos == bannerAdapter.getCount() - 1) {
					bannerViewPager.post(new Runnable() {
						@Override
						public void run() {
							bannerViewPager.setCurrentItem(
									getTargetItemPostion(pos), false);
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
		checkPoint(getDataPostion(pos));
	}

	private void addPoint(int amount) {
		llSelectFlagContaner.removeAllViews();
		for (int i = 0; i < amount; ++i) {
			ImageView pointView = new ImageView(getContext());
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT);
			final int leftMargin = (i == 0) ? 0 : getResources()
					.getDimensionPixelSize(R.dimen.banner_point_space);
			params.setMargins(leftMargin, 0, 0, 0);
			pointView.setLayoutParams(params);
			pointView.setImageDrawable(getResources().getDrawable(
					R.drawable.bannner_point));

			llSelectFlagContaner.addView(pointView);
		}
	}

	private void checkPoint(int pos) {
		for (int i = 0; i < llSelectFlagContaner.getChildCount(); ++i) {
			llSelectFlagContaner.getChildAt(i).setSelected(i == pos);
		}
	}

	private Timer timer;

	private TimerTask task;

	public void startAutoPlay() {
		stopAutoPlay();

		if (timer == null) {
			timer = new Timer("auto_play_banner");
		}

		task = new TimerTask() {
			@Override
			public void run() {
				bannerViewPager.post(new Runnable() {
					@Override
					public void run() {
						if (bannerAdapter.getCount() != 0) {
							bannerViewPager.setCurrentItem((bannerViewPager
									.getCurrentItem() + 1)
									% bannerAdapter.getCount());
						}
					}
				});
			}
		};
		timer.schedule(task, 5000, 5000);// 2 * DateUtils.MILLIS_PER_SECOND, 2 *
											// DateUtils.MILLIS_PER_SECOND);
	}

	public void stopAutoPlay() {
		if (task != null && timer != null) {
			task.cancel();
			timer.purge();
			task = null;
		}
	}

	/**
	 * 是否循环显示ViewPager的内容
	 * 
	 * @return
	 */
	boolean isViewPagerShownInLoop() {
		return boardAreas != null && boardAreas.size() > 1;
	}

	/**
	 * ViewPager循环显示时, 获取数据所在的位置
	 * 
	 * @param position
	 * @return
	 */
	int getDataPostion(int position) {
		if (isViewPagerShownInLoop() && !boardAreas.isEmpty()) {
			return (position + boardAreas.size() - 1) % boardAreas.size();
		}

		return position;
	}

	/**
	 * ViewPager循环显示时, 获取可能要跳转的位置(如果输入和输出不等, 则ViewPage需要跳转到输出的位置)
	 * 
	 * **************************************************************** Data: |
	 * 2 | 0 | 1 | 2 | 0 | Item: | 0 | 1 | 2 | 3 | 4 | TargetItem: | 3 | 1 | 2 |
	 * 3 | 1 | ****************************************************************
	 * 
	 * @param position
	 * @return
	 */
	int getTargetItemPostion(int position) {
		if (isViewPagerShownInLoop() && !boardAreas.isEmpty()) {
			if (position == 0) {
				position += boardAreas.size();
			} else if (position == boardAreas.size() + 1) {
				position -= boardAreas.size();
			}
		}

		return position;
	}

}
