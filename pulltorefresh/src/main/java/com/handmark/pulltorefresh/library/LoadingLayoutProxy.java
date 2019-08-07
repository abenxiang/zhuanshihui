package com.handmark.pulltorefresh.library;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.internal.BaseLoadingLayout;

import java.util.HashSet;


public class LoadingLayoutProxy implements ILoadingLayout {

  private final HashSet<BaseLoadingLayout> mLoadingLayouts;

  LoadingLayoutProxy() {
    mLoadingLayouts = new HashSet<BaseLoadingLayout>();
  }

  /**
   * This allows you to add extra LoadingLayout instances to this proxy. This is only necessary if
   * you keep your own instances, and want to have them included in any
   * {@link PullToRefreshBase#createLoadingLayoutProxy(boolean, boolean)
   * createLoadingLayoutProxy(...)} calls.
   * 
   * @param layout - LoadingLayout to have included.
   */
  public void addLayout(BaseLoadingLayout layout) {
    if (null != layout) {
      mLoadingLayouts.add(layout);
    }
  }

  @Override
  public void setLastUpdatedLabel(CharSequence label) {
    for (BaseLoadingLayout layout : mLoadingLayouts) {
      layout.setLastUpdatedLabel(label);
    }
  }

  @Override
  public void setLoadingDrawable(Drawable drawable) {
    for (BaseLoadingLayout layout : mLoadingLayouts) {
      layout.setLoadingDrawable(drawable);
    }
  }

  @Override
  public void setRefreshingLabel(CharSequence refreshingLabel) {
    for (BaseLoadingLayout layout : mLoadingLayouts) {
      layout.setRefreshingLabel(refreshingLabel);
    }
  }

  @Override
  public void setPullLabel(CharSequence label) {
    for (BaseLoadingLayout layout : mLoadingLayouts) {
      layout.setPullLabel(label);
    }
  }

  @Override
  public void setReleaseLabel(CharSequence label) {
    for (BaseLoadingLayout layout : mLoadingLayouts) {
      layout.setReleaseLabel(label);
    }
  }

  public void setTextTypeface(Typeface tf) {
    for (BaseLoadingLayout layout : mLoadingLayouts) {
      layout.setTextTypeface(tf);
    }
  }
}
