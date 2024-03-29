package com.handmark.pulltorefresh.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.handmark.pulltorefresh.library.R;

import java.io.InputStream;

public class GifImageView extends AppCompatImageView implements OnClickListener {
	/**
	 * 播放GIF动画的关键类
	 */
	private Movie	mMovie;

	/**
	 * 记录动画开始的时间
	 */
	private long	mMovieStart;

	/**
	 * GIF图片的宽度
	 */
	private int		mImageWidth;

	/**
	 * GIF图片的高度
	 */
	private int		mImageHeight;

	/**
	 * 图片是否正在播放
	 */
	private boolean	isPlaying;

	/**
	 * 是否允许自动播放
	 */
	private boolean	isAutoPlay;

	public GifImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public GifImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public GifImageView(Context context) {
		super(context);
		init(context, null);
	}

	@SuppressLint("Recycle")
	private void init(Context context, AttributeSet attrs) {
		if(attrs == null) {
			return;
		}

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.GifImageView);
		int resourceId = a.getResourceId(R.styleable.GifImageView_android_src, 0);
		if (resourceId != 0) {
			disableHardAware();
			// 当资源id不等于0时，就去获取该资源的流
			InputStream is = getResources().openRawResource(resourceId);
			// 使用Movie类对流进行解码
			mMovie = Movie.decodeStream(is);
			if (mMovie != null) {
				// 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性
				isAutoPlay = a.getBoolean(R.styleable.GifImageView_auto_play,
						false);
				mImageWidth = mMovie.width();
				mImageHeight = mMovie.height()               ;
				if (!isAutoPlay) {
					// 当不允许自动播放的时候，得到开始播放按钮的图片，并注册点击事件
					setOnClickListener(this);
				}
			}
		}
	}

	@SuppressLint("NewApi")
	private void disableHardAware() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == getId()) {
			// 当用户点击图片时，开始播放GIF动画
			isPlaying = true;
			invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie == null) {
			// mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
			super.onDraw(canvas);
		} else {
			// mMovie不等于null，说明是张GIF图片
			if (isAutoPlay) {
				// 如果允许自动播放，就调用playMovie()方法播放GIF动画
				playMovie(canvas);
				invalidate();
			} else {
				// 不允许自动播放时，判断当前图片是否正在播放
				if (isPlaying) {
					// 正在播放就继续调用playMovie()方法，一直到动画播放结束为止
					if (playMovie(canvas)) {
						isPlaying = false;
					}
					invalidate();
				} else {
					// 还没开始播放就只绘制GIF图片的第一帧，并绘制一个开始按钮
					mMovie.setTime(0);
					mMovie.draw(canvas, 0, 0);
				}
			}
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
	 * 开始播放GIF动画，播放完成返回true，未完成返回false。
	 * 
	 * @param canvas
	 * @return 播放完成返回true，未完成返回false。
	 */
	private boolean playMovie(Canvas canvas) {
		long now = SystemClock.uptimeMillis();
		if (mMovieStart == 0) {
			mMovieStart = now;
		}
		int duration = mMovie.duration();
		if (duration == 0) {
			duration = 1000;
		}
		int relTime = (int) ((now - mMovieStart) % duration);
		mMovie.setTime(relTime);
		mMovie.draw(canvas, 0, 0);
		if ((now - mMovieStart) >= duration) {
			mMovieStart = 0;
			return true;
		}
		return false;
	}

}
