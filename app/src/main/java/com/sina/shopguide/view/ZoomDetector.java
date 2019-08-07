package com.sina.shopguide.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.sina.shopguide.util.MultiTouchUtils.MotionEventUtils;

/**
 * Zoom gesture detector
 * 
 * @author SinaDev
 * 
 */
public class ZoomDetector {
	private MotionEvent		mEvDown;

	private MotionEvent		mEvDownPointer;

	private boolean			mIsZoomDetected	= false;

	private double mOldDist		= 1f;

	private OnZoomListener	mZoomListener;

	private int				mTouchSlop;

	private PointF			mMidPoint		= new PointF();

	interface OnZoomListener {
		boolean onZoomStart(MotionEvent evDown, MotionEvent evDownPointer);

		boolean onZoom(MotionEvent evDown, MotionEvent evDownPointer,
                       MotionEvent event, PointF midPoint, float scale);

		boolean onZoomEnd(MotionEvent evDown, MotionEvent evDownPointer,
                          MotionEvent event);
	}

	@SuppressWarnings("deprecation")
	public ZoomDetector(Context context, OnZoomListener zoomListener) {
		mTouchSlop = ViewConfiguration.getTouchSlop();

		mZoomListener = zoomListener;
	}

	@SuppressLint("Recycle")
	public boolean onTouchEvent(MotionEvent event) {
		// Handle touch events here...
		int action = event.getAction() & MotionEventUtils.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mEvDown = MotionEvent.obtain(event);
			break;

		case MotionEvent.ACTION_MOVE:
			if (mIsZoomDetected) {
				double newDist = spacing(event);
				if (newDist > mTouchSlop) {
					double scale = newDist / mOldDist;
					if (mZoomListener != null) {
						return mZoomListener.onZoom(mEvDown, mEvDownPointer,
								event, mMidPoint, (float) scale);
					}
				}
			}
			break;

		default:
			if (action == MotionEventUtils.ACTION_POINTER_DOWN) {
				mIsZoomDetected = true;
				mEvDownPointer = MotionEvent.obtain(event);
				mOldDist = spacing(event);
				if (mOldDist > 10f) {
					midPoint(mMidPoint, event);
				}

				if (mZoomListener != null) {
					mZoomListener.onZoomStart(mEvDown, mEvDownPointer);
				}

				return true;
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEventUtils.ACTION_POINTER_UP) {
				mIsZoomDetected = false;

				if (mZoomListener != null) {
					mZoomListener.onZoomEnd(mEvDown, mEvDownPointer, event);
				}
			}
			break;
		}

		return false;
	}

	/** Determine the space between the first two fingers */
	protected static double spacing(MotionEvent event) {
		float x = MotionEventUtils.getX(event, 0)
				- MotionEventUtils.getX(event, 1);
		float y = MotionEventUtils.getY(event, 0)
				- MotionEventUtils.getY(event, 1);
		return Math.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	protected static void midPoint(PointF point, MotionEvent event) {
		float x = MotionEventUtils.getX(event, 0)
				+ MotionEventUtils.getX(event, 1);
		float y = MotionEventUtils.getY(event, 0)
				+ MotionEventUtils.getY(event, 1);
		point.set(x / 2, y / 2);
	}
}
