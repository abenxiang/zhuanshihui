package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.ZhuantiDetailActivity;
import com.sina.shopguide.dto.zhuanti;
import com.sina.shopguide.util.ImageLoaderUtils;

public class HomeTopicItemVView extends RelativeLayout    implements
					IUpdate<zhuanti>{
	private ImageView ivImgHot;
	private TextView ivDesc;
	private TextView tvTitle;

	private zhuanti topic;
	
	@Override
	public void update(zhuanti data) {
		if (data == null || topic == data) {
			return;
		}
		topic = data;
		update();
	}
	
	private void update(){
		ImageLoader.getInstance().displayImage(topic.getPicUrl(), ivImgHot,
				ImageLoaderUtils.getDefaultoptions());
		ivDesc.setText(topic.getSubtitle());
		tvTitle.setText(topic.getTitle());
	}

	public HomeTopicItemVView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HomeTopicItemVView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HomeTopicItemVView(Context context) {
		super(context);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.vw_home_topic_v_item, this);
		ivImgHot= (ImageView) findViewById(R.id.img_hot);
		ivDesc= (TextView) findViewById(R.id.tv_desc);
		tvTitle= (TextView) findViewById(R.id.tv_title);
		this.setBackgroundColor(getResources().getColor(R.color.colorWhite));

		setOnClickListener(mClick);
	}
	
	private OnClickListener mClick = new OnClickListener() {
		public void onClick(View v) {
			Intent in = new Intent(getContext(), ZhuantiDetailActivity.class);
			in.putExtra(ZhuantiDetailActivity.PID, topic.getId());
			getContext().startActivity(in);
		}
	};
	
}
