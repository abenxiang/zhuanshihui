package com.sina.shopguide.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BannerViewPager extends ViewPager {

	private OnTouchListener onDispatchTouchEventListener;

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BannerViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean handled = false;
		if(onDispatchTouchEventListener != null) {
			handled = onDispatchTouchEventListener.onTouch(this, ev);
		}
		if(!handled) {
			handled = super.dispatchTouchEvent(ev);
		}

		return handled;
	}

	public void setOnDispatchTouchEventListener(
			OnTouchListener onDispatchTouchEventListener) {
		this.onDispatchTouchEventListener = onDispatchTouchEventListener;
	}

}
