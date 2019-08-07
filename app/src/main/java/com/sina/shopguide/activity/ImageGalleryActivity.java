package com.sina.shopguide.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.shopguide.R;
import com.sina.shopguide.util.ImageLoaderUtils;
import com.sina.shopguide.view.TouchImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 浏览大图
 * @author SinaDev
 *
 */
public class ImageGalleryActivity extends BaseActivity implements OnPageChangeListener, OnClickListener{

	public static final String EXTRA_KEY_IMAGE_LIST = "image_list";

	public static final String EXTRA_KEY_CURRENT_INDEX = "current_index";

	private TextView tvTitle;

	private ViewPager vpImagePages;

	private ImageGalleryAdapter adapter;

	private List<String> imageList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<String> imgs = getIntent().getStringArrayListExtra(EXTRA_KEY_IMAGE_LIST);
		if(imgs != null) {
			imageList.addAll(imgs);
		}

		setContentView(R.layout.activity_image_gallery);

		tvTitle = (TextView) findViewById(R.id.tv_title);

		vpImagePages = (ViewPager) findViewById(R.id.vp_img_pages);
		adapter = new ImageGalleryAdapter();
		vpImagePages.setOnPageChangeListener(this);
		vpImagePages.setAdapter(adapter);

		int currentIndex = getIntent().getIntExtra(EXTRA_KEY_CURRENT_INDEX, 0);
		vpImagePages.setCurrentItem(currentIndex);

		updateTitle();
	}

	private class ImageGalleryAdapter extends PagerAdapter {

		private SparseArray<WeakReference<TouchImageView>> imageViewMap = new SparseArray<WeakReference<TouchImageView>>(imageList.size());

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			WeakReference<TouchImageView> ivRef = imageViewMap.get(position);
			TouchImageView iv = (ivRef != null) ? ivRef.get() : null;
			if(iv == null) {
				iv = new TouchImageView(ImageGalleryActivity.this);
				imageViewMap.put(position, new WeakReference<TouchImageView>(iv));
				iv.setOnClickListener(ImageGalleryActivity.this);
			}
			container.addView(iv, 0);
			ImageLoader.getInstance().displayImage(imageList.get(position), iv,
					ImageLoaderUtils.getDefaultoptions(),
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							if(loadedImage != null) {
								perfectDisplay((TouchImageView) view, loadedImage.getWidth(), loadedImage.getHeight());
							}
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
						}
					});
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

	/**
	 * 自适应View的宽度(这里实际为屏幕宽度)，进行显示
	 * 
	 * @param ivToDisplay
	 */
	private static void perfectDisplay(TouchImageView ivToDisplay,
			int bitmapWidth, int bitmapHeight) {
		int viewWidth = ivToDisplay.getWidth();
		int viewHeight = ivToDisplay.getHeight();

		if (viewWidth == 0 || bitmapWidth == 0) {
			return;
		}

		Matrix matrix = new Matrix();
		float scale = viewWidth / (float) bitmapWidth;
		matrix.postScale(scale, scale, 0, 0);

		// 调整scale的极限值
		if (scale < ivToDisplay.getMinScale()) {
			ivToDisplay.setMinScale(scale);
		}
		if (scale > ivToDisplay.getMaxScale()) {
			ivToDisplay.setMaxScale(scale);
		}

		int scaledHeight = (int) (bitmapHeight * scale);
		int y = (scaledHeight > viewHeight) ? 0
				: (viewHeight - scaledHeight) / 2;
		matrix.postTranslate(0, y);

		ivToDisplay.setImageMatrix(matrix);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		updateTitle();
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	private void updateTitle() {
		tvTitle.setText(String.format("%d/%d", vpImagePages.getCurrentItem() + 1, imageList.size()));
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
