package com.handmark.pulltorefresh.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.R;

import java.io.InputStream;


/**
 * 用gif作为进度条的容器
 * @author SinaDev
 *
 */
public class GifProgressView extends AppCompatImageView {

	/**
	 * 播放GIF动画的关键类
	 */
	private Movie	mMovie;

	/**
	 * GIF图片的宽度
	 */
	private int		mImageWidth;

	/**
	 * GIF图片的高度
	 */
	private int		mImageHeight;

	private int		mProgress;

	public GifProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public GifProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public GifProgressView(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		if(attrs == null) {
			return;
		}

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.GifImageView);
		int resourceId = a.getResourceId(R.styleable.GifProgressView_android_src, 0);
		if (resourceId != 0) {
			disableHardAware();
			// 当资源id不等于0时，就去获取该资源的流
			InputStream is = getResources().openRawResource(resourceId);
			// 使用Movie类对流进行解码
			mMovie = Movie.decodeStream(is);
			if (mMovie != null) {
				mImageWidth = mMovie.width();
				mImageHeight = mMovie.height();
			}
		}
		mProgress = a.getInt(R.styleable.GifProgressView_progress, 0);

		a.recycle();
	}

	@SuppressLint("NewApi")
	private void disableHardAware() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie == null) {
			// mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
			super.onDraw(canvas);
		} else {
			onProgressChanged(canvas, mProgress);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mMovie != null) {
			// 如果是GIF图片则重写设定View的大小
			setMeasuredDimension(mImageWidth, mImageHeight);
		}
		else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * 显示进度
	 * @param canvas
	 * @param progress
	 */
	private void onProgressChanged(Canvas canvas, int progress) {
		int duration = mMovie.duration();
		if (duration == 0) {
			duration = 1000;
		}
		int relTime = duration * progress / 100;
		mMovie.setTime(relTime);
		mMovie.draw(canvas, 0, 0);
	}

	public void setProgress(int progress) {
		mProgress = Math.min(progress, 100);
		invalidate();
	}
}
