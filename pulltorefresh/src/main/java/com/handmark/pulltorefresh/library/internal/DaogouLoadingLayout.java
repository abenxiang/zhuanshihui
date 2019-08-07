/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.view.GifImageView;
import com.handmark.pulltorefresh.library.view.GifProgressView;

@SuppressLint("ViewConstructor")
public class DaogouLoadingLayout extends BaseLoadingLayout {

	private FrameLayout mInnerLayout;

	protected final GifProgressView mHeaderImage;
	protected final GifImageView mHeaderProgress;

	private final TextView mHeaderText;

	protected final Mode mMode;

	private CharSequence mPullLabel;
	private CharSequence mRefreshingLabel;
	private CharSequence mReleaseLabel;

	public DaogouLoadingLayout(Context context, final Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context);
		mMode = mode;

		LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_daogou_vertical, this);

		mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
		mHeaderProgress = (GifImageView) mInnerLayout.findViewById(R.id.pull_refresh_progress);
		mHeaderImage = (GifProgressView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

		LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

		switch (mode) {
			case PULL_FROM_END:
				lp.gravity = Gravity.TOP;

				// Load in labels
				mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label);
				mRefreshingLabel = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
				mReleaseLabel = context.getString(R.string.pull_to_refresh_from_bottom_release_label);
				break;

			case PULL_FROM_START:
			default:
				lp.gravity = Gravity.BOTTOM;

				// Load in labels
				mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
				mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
				mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);
				break;
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
			if (null != background) {
				ViewCompat.setBackground(this, background);
			}
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
			setTextAppearance(styleID.data);
		}

		// Text Color attrs need to be set after TextAppearance attrs
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
			if (null != colors) {
				setTextColor(colors);
			}
		}

		// Try and get defined drawable from Attrs
		Drawable imageDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
			imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
		}

		// Check Specific Drawable from Attrs, these overrite the generic
		// drawable attr above
		switch (mode) {
			case PULL_FROM_START:
			default:
				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
					Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
				}
				break;

			case PULL_FROM_END:
				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
					Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
				}
				break;
		}

		// If we don't have a user defined drawable, load the default
		if (null == imageDrawable) {
			imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
		}

		// Set Drawable, and save width/height
		setLoadingDrawable(imageDrawable);

		reset();

		mHeaderImage.setProgress(0);

	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		return mInnerLayout.getHeight();
	}

	public final void hideAllViews() {
		if (View.VISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
	}

	public final void onPull(float scaleOfLayout) {
		onPullImpl(scaleOfLayout);
	}

	public final void pullToRefresh() {
		if (null != mHeaderText) {
			if(mMode == Mode.PULL_FROM_START){
				mHeaderImage.setVisibility(View.VISIBLE);
				mHeaderText.setText(mPullLabel);
				setLastUpdatedLabel(null);
			}else{
				mHeaderText.setText(mRefreshingLabel);
				setLastUpdatedLabel("");
				mHeaderImage.setVisibility(View.INVISIBLE);
				mHeaderProgress.setVisibility(View.VISIBLE);
			}
		}

		// Now call the callback
		pullToRefreshImpl();
		
	}

	public final void refreshing() {
		if (mMode != Mode.DISABLED) {
			if (null != mHeaderText) {
				mHeaderText.setText(mRefreshingLabel);
				setLastUpdatedLabel("");
			}
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
		// Now call the callback
		refreshingImpl();

	}

	public final void releaseToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mReleaseLabel);
			setLastUpdatedLabel(null);
		}

		// Now call the callback
		releaseToRefreshImpl();
	}

	public final void reset() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
			setLastUpdatedLabel(null);
		}
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderText.setText("");
		setLastUpdatedLabel("");

		// Now call the callback
		resetImpl();
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
	}

	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Now call the callback
		onLoadingDrawableSet(imageDrawable);
	}

	public void setPullLabel(CharSequence pullLabel) {
		mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	@Override
	public void setTextTypeface(Typeface tf) {
		mHeaderText.setTypeface(tf);
	}

	public final void showInvisibleViews() {
		if (View.INVISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Callbacks for derivative Layouts
	 */

	public void onLoadingDrawableSet(Drawable imageDrawable) {
	}

	protected void onPullImpl(float scaleOfLayout) {
		if(scaleOfLayout < 0.5) {
			return;
		}
		int progress = Math.min((int) ((scaleOfLayout - 0.5) * 2 * 100), 100);
		mHeaderImage.setProgress(100 - progress);
	}

	protected void pullToRefreshImpl() {
		// NO-OP
	}

	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}

	protected void refreshingImpl() {
		if(mMode != Mode.PULL_FROM_END){
			mHeaderImage.clearAnimation();
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	protected void resetImpl() {
		mHeaderProgress.setVisibility(View.GONE);
		if (mMode != Mode.PULL_FROM_END) {
			mHeaderImage.setVisibility(View.VISIBLE);
		}

		mHeaderImage.setProgress(100);
		mHeaderImage.setVisibility(View.INVISIBLE);
	}

	private void setTextAppearance(int value) {
		if (null != mHeaderText) {
			mHeaderText.setTextAppearance(getContext(), value);
		}
	}

	private void setTextColor(ColorStateList color) {
		if (null != mHeaderText) {
			mHeaderText.setTextColor(color);
		}
	}

}
