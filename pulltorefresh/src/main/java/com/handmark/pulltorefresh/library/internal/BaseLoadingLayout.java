package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;

public abstract class BaseLoadingLayout extends FrameLayout implements ILoadingLayout {

	public BaseLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BaseLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseLoadingLayout(Context context) {
		super(context);
	}

	public abstract void reset();

	public abstract int getContentSize();

	public abstract void setWidth(int maximumPullScroll);

	public abstract void setHeight(int maximumPullScroll);

	public abstract void pullToRefresh();

	public abstract void refreshing();

	public abstract void releaseToRefresh();

	public abstract void onPull(float scale);

	public abstract void hideAllViews();

	public abstract void showInvisibleViews();

}
