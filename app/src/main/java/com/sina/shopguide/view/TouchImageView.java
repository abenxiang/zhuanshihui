package com.sina.shopguide.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

import com.sina.shopguide.util.MultiTouchUtils;

/**
 * 支持多点触控的ImageView
 * 
 * @author SinaDev
 * 
 */
public class TouchImageView extends AppCompatImageView implements OnGestureListener,
		OnDoubleTapListener, ZoomDetector.OnZoomListener {
	@SuppressWarnings("unused")
	private static final String	TAG				= TouchImageView.class
														.getSimpleName();

	// Scale range
	private float				mMinScale		= 1f;
	private float				mMaxScale		= 4f;

	// These matrices will be used to move and zoom image
	private Matrix				mCurMatrix		= new Matrix();
	private Matrix				mSavedMatrix	= new Matrix();

	private GestureDetector		mGestureDetector;

	private ZoomDetector		mZoomDetector;

	private Bitmap				mBitmap;

	private boolean				mZoomTrigged;

	/**
	 * Executes the delta scrolls from a fling or scroll movement.
	 */
	private FlingRunnable		mFlingRunnable	= new FlingRunnable();

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * @param context
	 */
	public TouchImageView(Context context) {
		super(context);
		init();
	}

	protected void init() {
		setScaleType(ScaleType.MATRIX);

		mGestureDetector = new GestureDetector(getContext(), this);
		mGestureDetector.setIsLongpressEnabled(false);

		mZoomDetector = new ZoomDetector(getContext(), this);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isBitmapNotExists()) {
			return false;
		}

		int action = event.getAction() & MultiTouchUtils.MotionEventUtils.ACTION_MASK;
		if (action == MotionEvent.ACTION_DOWN) {
			mFlingRunnable.stop();
		}

		boolean assume = false;
		if (isEnabled()) {
			assume = mZoomDetector.onTouchEvent(event);
		}

		mGestureDetector.onTouchEvent(event);

		if (action == MotionEvent.ACTION_DOWN) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (action == MotionEvent.ACTION_UP && isEnabled()) {
			checkNeedScroll();
		}

		if (action == MotionEvent.ACTION_DOWN) {
			mZoomTrigged = false;
			return true;
		}

		return assume;
	}

	@Override
	public boolean onZoomStart(MotionEvent evDown, MotionEvent evDownPointer) {
		mZoomTrigged = true;
		mSavedMatrix.set(mCurMatrix);
		return true;
	}

	@Override
	public boolean onZoomEnd(MotionEvent evDown, MotionEvent evDownPointer,
			MotionEvent event) {
		mSavedMatrix.set(mCurMatrix);
		return true;
	}

	@Override
	public boolean onZoom(MotionEvent evDown, MotionEvent evDownPointer,
			MotionEvent event, PointF midPoint, float scale) {
		if (isBitmapNotExists()) {
			return false;
		}

		mCurMatrix.set(mSavedMatrix);

		float currentScale = getCurrentScale();
		float resultScale = currentScale * scale;
		// 控制图片大小
		if (resultScale < mMinScale) {
			scale = mMinScale / currentScale;
		} else if (resultScale > mMaxScale) {
			scale = mMaxScale / currentScale;
		}
		// SCALE
		mCurMatrix.postScale(scale, scale, midPoint.x, midPoint.y);

		center(true, true);

		setImageMatrix(mCurMatrix);

		return true;
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if(drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			mBitmap = bd.getBitmap();
			if (mBitmap != null) {
				center(true, true);
				setImageMatrix(mCurMatrix);
			}
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		if (mBitmap != null) {
			center(true, true);
			setImageMatrix(mCurMatrix);
		}
	}

	@Override
	public void setImageResource(int resId) {
		final Drawable drawable = getResources().getDrawable(resId);
		final float dx = (getWidth() - drawable.getMinimumWidth()) / 2;
		final float dy = (getHeight() - drawable.getMinimumHeight()) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(dx, dy);
		setImageMatrix(matrix);
		setImageDrawable(drawable);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// fit to screen width
		if (isBitmapNotExists()) {
			return;
		}

		float scale = getWidth() / (float) getBitmapWidth();
		float relativeScale = scale / getCurrentScale();
		mCurMatrix.postScale(relativeScale, relativeScale, 0, 0);
		center(true, true);
		setImageMatrix(mCurMatrix);
	}

	protected float getCurrentScale() {
		float p[] = new float[9];
		mCurMatrix.getValues(p);
		return p[Matrix.MSCALE_X];
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (!isEnabled()) {
			return false;
		}

		if (isBitmapNotExists()) {
			return false;
		}

		int initialVelocityX = 0;
		int initialVelocityY = 0;
		// 只在速度较大的轴上滑动
		if (Math.abs(velocityX) > Math.abs(velocityY)) {
			initialVelocityX = (int) velocityX;
			initialVelocityY = 0;
		} else {
			initialVelocityX = 0;
			initialVelocityY = (int) velocityY;
		}

		mSavedMatrix.set(mCurMatrix);
		mFlingRunnable.startUsingVelocity(initialVelocityX, initialVelocityY);

		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		final RectF currentRect = getCurrentRect();
		if (Math.abs(distanceX) > Math.abs(distanceY)) {
			if ((currentRect.left > -1 && distanceX < 0)
					|| (currentRect.right < getWidth() + 1 && distanceX > 0)) {
				getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		}

		if (!isEnabled()) {
			return false;
		}

		if (isBitmapNotExists()) {
			return false;
		}

		if (isScroolToBottom() || isScroolToTop()) {
			distanceY /= 3;
		}
		mCurMatrix.postTranslate(-distanceX, -distanceY);

		center(true, false);

		setImageMatrix(mCurMatrix);

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (!mZoomTrigged) {
			return performClick();
		}
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (!isEnabled()) {
			return false;
		}

		if (isBitmapNotExists()) {
			return false;
		}

		// switch between full-screen size and 4 * screen width
		float scale = getWidth() / (float) getBitmapWidth();;
		if (Double.compare(getCurrentScale(), scale) == 0) {
			scale *= 4;
		}

		float relativeScale = scale / getCurrentScale();
		mCurMatrix.postScale(relativeScale, relativeScale, e.getX(), e.getY());

		center(true, true);

		setImageMatrix(mCurMatrix);
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) {
		if (isBitmapNotExists()) {
			return;
		}

		RectF rect = getCurrentRect();
		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下放留空则往下移
			int ivHeight = getHeight();
			if (height < ivHeight) {
				deltaY = (ivHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < ivHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int ivWidth = getWidth();
			if (width < ivWidth) {
				deltaX = (ivWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < ivWidth) {
				deltaX = ivWidth - rect.right;
			}
		}
		mCurMatrix.postTranslate(deltaX, deltaY);
	}

	private RectF getCurrentRect() {
		Matrix m = new Matrix();
		m.set(mCurMatrix);
		RectF rect = new RectF(0, 0, getBitmapWidth(), getBitmapHeight());
		m.mapRect(rect);
		return rect;
	}

	public float getMinScale() {
		return mMinScale;
	}

	public void setMinScale(float minScale) {
		mMinScale = minScale;
	}

	public float getMaxScale() {
		return mMaxScale;
	}

	public void setMaxScale(float maxScale) {
		mMaxScale = maxScale;
	}

	@Override
	public void setImageMatrix(Matrix matrix) {
		if (mCurMatrix != matrix) {
			mCurMatrix = matrix;
		}
		center(true, false);
		super.setImageMatrix(matrix);
	}

	/**
	 * Responsible for fling behavior. A FlingRunnable will keep re-posting
	 * itself until the fling is done.
	 * 
	 */
	private class FlingRunnable implements Runnable {
		/**
		 * Tracks the decay of a fling scroll
		 */
		private Scroller	mScroller;

		/**
		 * X value reported by mScroller on the previous fling
		 */
		private int			mLastFlingX;

		/**
		 * Y value reported by mScroller on the previous fling
		 */
		private int			mLastFlingY;

		public FlingRunnable() {
			mScroller = new Scroller(getContext());
		}

		private void startCommon() {
			// Remove any pending flings
			removeCallbacks(this);
		}

		public void startUsingVelocity(int initialVelocityX,
				int initialVelocityY) {
			if (initialVelocityX == 0 && initialVelocityY == 0)
				return;

			startCommon();

			int initialX = initialVelocityX < 0 ? Integer.MAX_VALUE : 0;
			mLastFlingX = initialX;

			int initialY = initialVelocityY < 0 ? Integer.MAX_VALUE : 0;
			mLastFlingY = initialY;

			mScroller.fling(initialX, initialY, initialVelocityX,
					initialVelocityY, 0, Integer.MAX_VALUE, 0,
					Integer.MAX_VALUE);
			post(this);
		}

		public void stop() {
			removeCallbacks(this);
			endFling();
		}

		private void endFling() {
			mScroller.forceFinished(true);
		}

		public void run() {
			if (isBitmapNotExists()) {
				endFling();
				return;
			}

			final Scroller scroller = mScroller;
			boolean more = scroller.computeScrollOffset();
			final int x = scroller.getCurrX();
			final int y = scroller.getCurrY();

			// Flip sign to convert finger direction to list items direction
			// (e.g. finger moving down means list is moving towards the top)
			int deltaX = x - mLastFlingX;
			if (deltaX > 0) {
				deltaX = Math.min(getWidth(), deltaX);
			} else {
				deltaX = Math.max(-getWidth(), deltaX);
			}

			int deltaY = y - mLastFlingY;
			if (deltaY > 0) {
				deltaY = Math.min(getHeight(), deltaY);
			} else {
				deltaY = Math.max(-getHeight(), deltaY);
			}

			trackMotionScroll(deltaX, deltaY);

			if (more) {
				mLastFlingX = x;
				mLastFlingY = y;
				post(this);
			} else {
				endFling();
			}
		}
	}

	private void trackMotionScroll(int deltaX, int deltaY) {
		mCurMatrix.set(mSavedMatrix);
		mCurMatrix.postTranslate(deltaX, deltaY);

		center(true, true);

		setImageMatrix(mCurMatrix);
		mSavedMatrix.set(mCurMatrix);
	}

	protected boolean isBitmapNotExists() {
		return mBitmap == null;
	}

	protected int getBitmapWidth() {
		return mBitmap.getWidth();
	}

	protected int getBitmapHeight() {
		return mBitmap.getHeight();
	}

	public void stopFling() {
		if (mFlingRunnable != null) {
			mFlingRunnable.stop();
		}
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	private boolean isScroolToTop() {
		final RectF currentRect = getCurrentRect();
		if (isOneScreen()) {
			return currentRect.top > (getHeight() - (currentRect.bottom - currentRect.top)) / 2 ? true
					: false;
		} else {
			return currentRect.top >= 0 ? true : false;
		}
	}

	private boolean isScroolToBottom() {
		final RectF currentRect = getCurrentRect();
		if (isOneScreen()) {
			return currentRect.bottom <= getHeight()
					- (getHeight() - (currentRect.bottom - currentRect.top))
					/ 2 ? true : false;
		} else {
			return currentRect.bottom <= getHeight() ? true : false;
		}
	}

	private boolean isOneScreen() {
		final RectF currentRect = getCurrentRect();
		return currentRect.bottom - currentRect.top < getHeight() ? true
				: false;
	}

	private void checkNeedScroll() {
		startTranslateAnimation();
	}

	private void startTranslateAnimation() {

		if (!(isScroolToTop() || isScroolToBottom())) {
			return;
		}
		final RectF currentRect = getCurrentRect();

		float fromYDelta = 0;
		float toYDelta = 0;

		if (isScroolToTop()) {
			if (isOneScreen()) {
				fromYDelta = currentRect.top
						- (getHeight() - (currentRect.bottom - currentRect.top))
						/ 2;
			} else {
				fromYDelta = currentRect.top;
			}
			toYDelta = 0;
		} else {
			if (isOneScreen()) {
				fromYDelta = currentRect.bottom
						- getBottom()
						+ (getHeight() - (currentRect.bottom - currentRect.top))
						/ 2;
			} else {
				fromYDelta = currentRect.bottom - getBottom();
			}
			toYDelta = 0;
		}

		TranslateAnimation trans = new TranslateAnimation(0, 0, fromYDelta,
				toYDelta);
		trans.setFillAfter(false);
		trans.setDuration(100);

		final float distance = fromYDelta - toYDelta;

		if (distance == 0) {
			return;
		}

		mCurMatrix.set(mSavedMatrix);
		mCurMatrix.postTranslate(0, distance);

		center(true, true);

		setImageMatrix(mCurMatrix);
		mSavedMatrix.set(mCurMatrix);
		this.startAnimation(trans);
	}
}