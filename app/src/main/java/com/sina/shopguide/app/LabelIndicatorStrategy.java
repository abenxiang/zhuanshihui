package com.sina.shopguide.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class LabelIndicatorStrategy {

  private Context context;

  private int resource;

  private final CharSequence title;

  public LabelIndicatorStrategy(Context context, CharSequence label) {
    this.context = context;
    this.title = label;
  }

  public LabelIndicatorStrategy(Context context, CharSequence title, int resource) {
    this(context, title);
    this.resource = resource;
  }

  public View createIndicatorView(TabHost tabHost) {
    View indicatorView =
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
            resource, tabHost.getTabWidget(), false);
    ((TextView) indicatorView.findViewById(android.R.id.title)).setText(title);
    return indicatorView;
  }
}
