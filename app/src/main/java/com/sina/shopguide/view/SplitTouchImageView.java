package com.sina.shopguide.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

/**
 * 支持将大图分块加载的ImageView,继承了TouchImageView的所有功能
 * 
 * @author SinaDev
 * 
 */
public class SplitTouchImageView extends TouchImageView {
	private boolean		mIsSplited;

	private Bitmap[]	mSplitedBitmaps;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SplitTouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SplitTouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public SplitTouchImageView(Context context) {
		super(context);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		mIsSplited = false;
		super.setImageBitmap(bm);
	}

	public void setSplitedBitmaps(Bitmap[] bitmaps) {
		mIsSplited = true;
		mSplitedBitmaps = bitmaps;
		center(true, true);
		invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (mIsSplited) {
			int bitmapTop = 0;
			if (mSplitedBitmaps != null) {
				for (Bitmap bmp : mSplitedBitmaps) {
					Matrix matrix = new Matrix();
					matrix.postConcat(getImageMatrix());
					matrix.postTranslate(0, bitmapTop * getCurrentScale());

					canvas.drawBitmap(bmp, matrix, null);

					bitmapTop += bmp.getHeight();
				}
			}
		} else {
			super.onDraw(canvas);
		}
	}

	@Override
	protected boolean isBitmapNotExists() {
		if (mIsSplited) {
			return mSplitedBitmaps == null || mSplitedBitmaps.length == 0;
		}

		return super.isBitmapNotExists();
	}

	@Override
	protected int getBitmapWidth() {
		if (mIsSplited) {
			if (isBitmapNotExists()) {
				return 0;
			}
			return mSplitedBitmaps[0].getWidth();
		}

		return super.getBitmapWidth();
	}

	@Override
	protected int getBitmapHeight() {
		if (mIsSplited) {
			if (isBitmapNotExists()) {
				return 0;
			}

			int height = 0;
			for (Bitmap bmp : mSplitedBitmaps) {
				height += bmp.getHeight();
			}
			return height;
		}

		return super.getBitmapHeight();
	}

	public Bitmap[] getSplitedBitmaps() {
		return mSplitedBitmaps;
	}
}
